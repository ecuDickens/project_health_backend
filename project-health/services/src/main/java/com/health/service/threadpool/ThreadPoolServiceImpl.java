package com.health.service.threadpool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.health.inject.Configuration;
import com.health.logging.Marker;
import com.health.spi.ThreadPoolService;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Singleton
public class ThreadPoolServiceImpl implements ThreadPoolService {

    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ThreadPoolServiceImpl.class);
    
    /** core threads */
    private final static String CORE_THREADS = "coreThreads";
    
    /** max threads */
    private final static String MAX_THREADS = "maxThreads";
    
    /** keep alive time */
    private final static String KEEP_ALIVE = "keepAlive";
    
    /** required fields */
    private final static List<String> REQUIRED_FIELDS = Lists.newArrayList(CORE_THREADS, MAX_THREADS, KEEP_ALIVE);
    
    /** thread pools */
    private Map<String, ThreadPoolExecutor> pools;

    @Inject
    public ThreadPoolServiceImpl(Configuration<ThreadPoolServiceImpl> configuration) {
        final Map<?, ?> config = configuration.getConfig();

        if (null == config) {
            String msg = "ThreadPoolService.yml file could not be found";
            LOGGER.error(Marker.insert(Marker.HEALTH_ERROR_INTERNAL, msg));
            throw new IllegalArgumentException(msg);
        }

        this.pools = Maps.newHashMapWithExpectedSize(config.size());
        for (final Object nameObject : config.keySet()) {
            final String name = (String) nameObject;
            final Map<?, ?> poolConfig = (Map<?, ?>) config.get(name);
            LOGGER.debug("pool {} config - {}", name, poolConfig);
            this.validatePoolConfig(name, poolConfig);

            // build it
            final Integer coreThreads = (Integer) poolConfig.get(CORE_THREADS);
            final Integer maxThreads = (Integer) poolConfig.get(MAX_THREADS);
            final Integer keepAlive = (Integer) poolConfig.get(KEEP_ALIVE);

            final ThreadPoolExecutor executor = new ThreadPoolExecutor(coreThreads, maxThreads, keepAlive, TimeUnit.MINUTES, new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
            executor.prestartAllCoreThreads();

            LOGGER.debug("Created thread pool {}, executor {}", name, executor);
            this.pools.put(name, executor);
        }
    }

    // validates the config
    private void validatePoolConfig(final String name, final Map<?, ?> poolConfig) {
        LOGGER.entry(name, poolConfig);
        for (final String field : REQUIRED_FIELDS) {
            if (!poolConfig.containsKey(field)) {
                String msg = "Pool config " + name + " must have " + field + " defined";
                LOGGER.error(Marker.insert(Marker.HEALTH_ERROR_INTERNAL, msg));
                throw new IllegalArgumentException(msg);
            }
        }
        LOGGER.exit();
    }
}

package com.health.inject;


import com.health.DomainConstants;
import com.health.Env;
import com.health.logging.Marker;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.health.configuration.spi.ConfigurationException;
import com.health.configuration.spi.ConfigurationLoader;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.lang.reflect.Type;
import java.util.Map;

public class Configuration<T> {

    private static final XLogger LOGGER = XLoggerFactory.getXLogger(Configuration.class);

    private Class<T> type;

    private final Env env;
    private final ConfigurationLoader configurationLoader;
    private Map<?,?> buildInMap;

    @Inject
    public Configuration(Env env, ConfigurationLoader configurationLoader) {
        this.env = env;
        this.configurationLoader = configurationLoader;
        buildInMap = null;
    }

    public Configuration(Env env, ConfigurationLoader configurationLoader, Type type) {
        this(env, configurationLoader);
        setType(type);
    }

    /**
     * Preset configuration, useful for tests and configs known apriori
     */
    public Configuration(Map<?, ?> map) {
        this.env = null;
        this.configurationLoader = null;
        this.buildInMap = map;
    }

    @SuppressWarnings("unchecked")
    public void setType(Type type) {
        Preconditions.checkArgument(type instanceof Class, "Type is not a Class");
        this.type = (Class<T>)type;
    }

    public Class<T> getType() {
        return type;
    }

    public Map<?, ?> getConfig() {
        LOGGER.entry();

        Map<?, ?> map;

        if(null != buildInMap) {
            map = buildInMap;
        }
        else {
            Preconditions.checkState(type != null, "Configuration type is unknown. Have you set the type of this configuration or called the correct constructor?");

            // lookup the config
            final String basePath = type.getName().replace('.', '/') + ".yml";
            String path = env.getConfigRoot() + '/' + basePath;
            LOGGER.debug("Loading config file {}", path);

            map = null;
            try {
                map = configurationLoader.getConfiguration(DomainConstants.FILE_PREFIX + path);
            } catch (ConfigurationException x) {
                if (env.isStandaloneJvm()) {
                    final String resourcePath = env.getConfigRootBase() + basePath;
                    try {
                        map = configurationLoader.getConfiguration(DomainConstants.RESOURCE_PREFIX + resourcePath);
                    } catch (ConfigurationException x2) {
                        LOGGER.error(Marker.insert(Marker.HEALTH_ERROR_INTERNAL,
                                "Cannot find configuration for class " + type.getCanonicalName()));
                    }
                }
            }

            // hand back the settings for the current domain if structured as such
            if (env.isDomainSet() && map != null && !map.isEmpty() && map.containsKey(env.getDomain())) {
                map = (Map<?, ?>) map.get(env.getDomain());
                LOGGER.debug("Domain configuration map: {}", map);
            }

            // store it
            buildInMap = map;
        }

        LOGGER.exit(map);
        return map;
    }

    @Override
    public String toString() {
        return "Configuration{type=" + type + "}";
    }
}

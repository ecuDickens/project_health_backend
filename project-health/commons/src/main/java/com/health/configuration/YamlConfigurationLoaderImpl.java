package com.health.configuration;

import com.health.DomainConstants;
import com.health.Env;
import com.health.logging.Marker;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import com.health.configuration.spi.ConfigurationLoadException;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

/**
 * ConfigurationLoader Provider for YAML files.  Supports file and resource via classloader.
 */
public class YamlConfigurationLoaderImpl extends BaseConfigurationLoader {

    private static final XLogger LOGGER = XLoggerFactory.getXLogger(YamlConfigurationLoaderImpl.class);

    private final Env env;

    @javax.inject.Inject // do NOT require Guice in this module
    public YamlConfigurationLoaderImpl(Env env) {
        this.env = env;
    }

    @Override
    public Map<?, ?> getConfiguration(final String name) throws ConfigurationLoadException {
        return getConfiguration(name, env.getEnvironment());
    }

    @Override
    public Map<?, ?> getConfiguration(final String name, final String environment) throws ConfigurationLoadException {
        LOGGER.entry(name, environment);

        Map<?, ?> map = null;
        InputStream is = null;
        Reader reader = null;

        try {
            // load the input yaml configuration file
            String contents;
            try {
                is = getAsInputStream(name);
                contents = CharStreams.toString(new InputStreamReader(is));
                reader = new StringReader(contents);
                map = (Map<?, ?>) new Yaml().load(reader);
                reader.reset();
            }
            catch (IOException e) {
                final String msg =
                        String.format("Unable to read configuration file %s for environment %s",
                                name, environment);
                LOGGER.error(Marker.insert(Marker.HEALTH_ERROR_INTERNAL, msg), e);
                throw new ConfigurationLoadException(msg, e);
            }

            // normalize and select the environment stanza
            map = (Map<?, ?>) normalize(map, environment);

            // interpolate any FreeMarkers embedded in the configuration
            YamlInterpolator markerProcessor = new YamlInterpolator(name, env);
            try {
                reader = markerProcessor.process(reader, extractInstanceDataMode(map));
                reader.reset();
                map = (Map<?, ?>) new Yaml().load(reader);
            }
            catch (IOException e) {
                final String msg =
                        String.format("Unable to interpolate data model for configuration file %s and environment %s",
                                name, environment);
                LOGGER.error(Marker.insert(Marker.HEALTH_ERROR_INTERNAL, msg), e);
                throw new ConfigurationLoadException(msg, e);
            }

            // re-normalize the evaluated configuration map
            map = normalize(map, environment);
        }
        finally {
            try {
                if (is != null) {
                    is.close();
                }

            } catch (IOException x) {
                LOGGER.error("Failed to close input stream to " + name, x);
            }
        }

        LOGGER.exit(map);
        return map;
    }

    private InputStream getAsInputStream(final String name) throws ConfigurationLoadException {
        InputStream is = null;

        if (name.startsWith(DomainConstants.FILE_PREFIX)) {
            String fileName = name.substring(DomainConstants.FILE_PREFIX.length());
            try {
                is = new FileInputStream(fileName);
            } catch (FileNotFoundException x) {
                throw new ConfigurationLoadException("Error loading configuration file " + fileName, x);
            }

        } else if (name.startsWith(DomainConstants.RESOURCE_PREFIX)) {
            String resourceName = name.substring(DomainConstants.RESOURCE_PREFIX.length());
            is = this.getClass().getResourceAsStream(resourceName);
        }

        if (is == null) {
            throw new ConfigurationLoadException("Cannot find resource: " + name);
        }

        return is;
    }

    private Map<String, Object> extractInstanceDataMode(Map map) {
        if (null == map || map.isEmpty()) {
            return Maps.newHashMap();
        }
        return Maps.filterEntries(map, new Predicate<Map.Entry<String, Object>>() {
            @Override
            public boolean apply(Map.Entry<String, Object> entry) {
                return entry.getKey() instanceof String && entry.getKey().toString().startsWith("_");
            }});
    }

}
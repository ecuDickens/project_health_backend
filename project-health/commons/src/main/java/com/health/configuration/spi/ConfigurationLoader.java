package com.health.configuration.spi;

import java.util.Map;

/**
 * Provider interface for configuration
 */
public interface ConfigurationLoader {
    
    /**
     * Retrieves the specified named configuration, normalized by the
     * <code>GWAPP_ENV_TYPE</code> environment variable
     * 
     * @param name name of the configuration
     * @return map of the configuration, <code>null</code> if it does not exist
     * @throws ConfigurationException error retrieving the configuration
     */
    public Map<?, ?> getConfiguration(final String name) throws ConfigurationException;

    /**
     * Retrieves the specified named configuration, normalized by the provided
     * environment
     * 
     * @param name
     *            name of the configuration
     * @param environment
     *            name of the environment, ie. development, test, or production
     * @return map of the configuration, <code>null</code> if it does not exist
     * @throws ConfigurationException error retrieving the configuration
     */
    public Map<?, ?> getConfiguration(final String name, final String environment) throws ConfigurationException;

}

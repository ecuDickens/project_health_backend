package com.health.configuration.spi;

/**
 * Exception which indicates an issue loading a configuration.
 */
public class ConfigurationLoadException extends ConfigurationException {
    /** serialization uid */
    private static final long serialVersionUID = -9065276765620769602L;

    /** default message */
    private static final String MESSAGE = "Unable to load configuration: %s";

    /**
     * Constructs a <code>ConfigurationLoadException</code> for the specified 
     * configuration file
     * 
     * @param configurationFile 
     *            the detail message  
     */
    public ConfigurationLoadException(String configurationFile) {
        super(formatMessage(configurationFile));     
    }

    /**
     * Constructs a <code>ConfigurationLoadException</code> for the specified
     * configuration file and exception that was raised.
     * 
     * @param configurationFile
     *            the detail message
     * @param cause
     *            the exception that was raised
     */
    public ConfigurationLoadException(String configurationFile, Throwable cause) {
        super(formatMessage(configurationFile), cause);
    }
    
    /**
     * Formats and returns a string containing the default message for this 
     * exception type, and includes in the resultant string the name of the 
     * configuration file that loading was attempted on. 
     * 
     * @param configurationFile  
     *      The name and/or path to the target configuration file.
     * @return 
     *      The formatted message for this exception type. 
     */
    private static String formatMessage(String configurationFile) {
        return String.format(MESSAGE, configurationFile);
    }
}
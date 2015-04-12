package com.health.configuration.spi;

/**
 * Exception which indicates a general problem with configuration.
 */
public class ConfigurationException extends Exception {
    /** serialization uid */
    private static final long serialVersionUID = -9065276765620769602L;

    /**
     * Constructs a <code>ConfigurationException</code> with the specified detail
     * message.
     * 
     * @param message 
     *            the detail message  
     */
    public ConfigurationException(String message) {
        super(message);        
    }

    /**
     * Constructs a <code>ConfigurationException</code> with the specified 
     * detail message and exception that was raised.
     * 
     * @param message
     *            the detail message
     * @param cause
     *            the exception that was raised
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
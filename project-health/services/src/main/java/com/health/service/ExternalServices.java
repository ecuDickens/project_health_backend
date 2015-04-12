package com.health.service;

import com.health.annotations.ExternalServiceOptions;
import com.health.spi.ExternalService;

/**
 * This class exposes methods for working with external services.
 */
public final class ExternalServices {

    private ExternalServices() {
    }

    /**
     * Returns the name of the supplied external service, by either taking the class
     * name minus the "Impl" suffix if an ExternalServiceOptions annotation has not
     * been provided, or by taking the name property value of the ExternalServiceOptions
     * if it has been provided.
     *
     * @param externalService
     *      The external service to return the name for.
     * @return
     *      A string representing the name of the external service.
     */
    public static String getName(final ExternalService externalService) {
        // parameter validations
        if (null == externalService) {
            throw new IllegalArgumentException("An external service must be provided.");
        }
        Class<? extends ExternalService> externalServiceClass = externalService.getClass();
        // try to resolve the external service options annotation from the external service interface
        ExternalServiceOptions externalServiceOptions =
                externalServiceClass.getAnnotation(ExternalServiceOptions.class);
        if (null == externalServiceOptions) {
            // default to class name minus impl suffix
            return externalServiceClass.getSimpleName().replace("Impl", "");
        }
        else {
            return externalServiceOptions.name();
        }
    }

}
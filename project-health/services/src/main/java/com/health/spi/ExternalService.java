package com.health.spi;

/**
 * Interface to define a service external to
 */
public interface ExternalService {

    /**
     * Tests the current connection to a service executing a single, cheap, idempotent call
     *
     * @throws Exception on any error contacting a service
     */
    void assertHealthy() throws Exception;

}

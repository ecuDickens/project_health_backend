package com.health.spi;

/**
 * A resource that provides a mechanism for ending its life-cycle safely
 * through explicit invocations.
 */
public interface Disposable {

    /**
     * Releases resources held by this implementation for safe garbage collection.
     */
    void dispose();
}

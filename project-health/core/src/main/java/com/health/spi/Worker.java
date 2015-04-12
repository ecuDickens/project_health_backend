package com.health.spi;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * A general interface that defines how an object may stop and start
 * some processing or handling, the exact meaning of which is implementation-specific
 * and is undefined by this interface.
 *
 */
public interface Worker {

    /**
     * Stops processing work. The semantics of this method are
     * implementation-specific and are undefined by this interface.
     *
     * It should be safe to call this method multiple times without calling start
     * in between invocations.
     *
     * Stopping an instance that is already stopped should be allowed and should
     * not produce an error state.
     */
    ListenableFuture<Boolean> stop();

    /**
     * Starts processing work. The semantics of this method are
     * implementation-specific and are undefined by this interface.
     *
     * It should be safe to call this method multiple times without calling stop
     * in between invocations.
     *
     * Starting an instance that is already started should be allowed and should
     * not produce an error state.
     */
    ListenableFuture<Boolean> start();
}

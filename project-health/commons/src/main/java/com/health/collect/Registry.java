package com.health.collect;


import com.google.common.collect.ImmutableSet;

/**
 * A simple collection of objects that is potentially mutable
 * but provides immutable point-in-time views of its contents.
 */
public interface Registry<T> {


    /**
     * Returns an ImmutableSet of the registered objects. Subsequent calls to
     * this method may return a different set with different contents, but
     * each call is a view of the registry's contents at that point in time.
     *
     * @return ImmutableSet of objects
     */
    public ImmutableSet<T> getRegistered();
}

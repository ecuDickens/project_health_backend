package com.health.collect;


import com.google.common.collect.ImmutableSet;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.util.WeakHashMap;

/**
 * A Registry implementation that provides methods for registering and
 * unregistering objects with the registry and is persisted in memory.
 */
public class InMemoryRegistry<T> implements Registry<T> {

    private final Object present = new Object();

    // don't build an internally thread-safe map, but optimize it optimistically;
    // synchronization is handled by this class's access to the internal map for
    // known breaking points
    private final WeakHashMap<T, Object> register = new WeakHashMap<T, Object>();

    public ImmutableSet<T> getRegistered() {
        return ImmutableSet.copyOf(register.keySet());
    }

    /**
     * Returns true if the target exists in the registry, false otherwise.
     *
     * @param target
     * @return
     */
    public boolean contains(T target) {
        // don't synchronize this call - should be okay
        return register.containsKey(target);
    }

    /**
     * Removes the target from the registry.
     *
     * @param target
     */
    public void unregister(T target) {
        // synchronize modifications to the map on the map itself
        synchronized(register) {
            register.remove(target);
        }
    }

    /**
     * Registers a target with the registry.
     *
     * @param target
     */
    public void register(T target) {
        // synchronize modifications to the map on the map itself
        synchronized(register) {
            register.put(target, present);
        }
    }
}

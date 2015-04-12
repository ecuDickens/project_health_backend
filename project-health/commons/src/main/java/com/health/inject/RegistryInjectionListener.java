package com.health.inject;


import com.health.collect.InMemoryRegistry;
import com.health.collect.Registry;
import com.google.inject.spi.InjectionListener;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Listens to injections of a particular type and registers each injected instance into an
 * internal registry for retrieval later. The registry holds a weak reference to each instance
 * so that they may still be garbage collected at any time.
 */
public class RegistryInjectionListener<T> implements InjectionListener<T> {

    private static final XLogger LOGGER = XLoggerFactory.getXLogger(RegistryInjectionListener.class);

    private final InMemoryRegistry<T> registry = new InMemoryRegistry<T>();

    @Override
    public void afterInjection(final T injectee) {
        LOGGER.debug("Registering injected instance: {}", injectee);
        registry.register(injectee);
    }

    /**
     * Returns a registry that contains instances that have been injected and tracked by
     * this listener.
     *
     * @return an Registry of registered instances
     */
    public Registry<T> getRegistry() {
        return registry;
    }
}

package com.health.inject;


import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class RegistryTypeListener<T> implements TypeListener {

    private static final XLogger LOGGER = XLoggerFactory.getXLogger(RegistryTypeListener.class);

    private final RegistryInjectionListener<T> listener;
    private final Class<? super T> typesToRegister;

    public RegistryTypeListener(final RegistryInjectionListener<T> listener, final Class<? super T> typesToRegister) {
        this.listener = listener;
        this.typesToRegister = typesToRegister;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I> void hear(final TypeLiteral<I> typeLiteral, final TypeEncounter<I> encounter) {
        final Class<? super I> rawType = typeLiteral.getRawType();
        if(typesToRegister.isAssignableFrom(rawType)) {
            LOGGER.debug("Registering typeLiteral encounter for {}", typeLiteral);
            ((TypeEncounter<T>)encounter).register(listener);
        }
    }
}

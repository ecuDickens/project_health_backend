package com.health.inject;


import com.health.collect.Registry;
import com.health.matchers.MoreMatchers;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class RegistryModule<T> extends AbstractModule implements Provider<Registry<T>> {

    private final TypeLiteral<Registry<T>> typeLiteral;
    private final Class<? super T> rawTClass;
    private final RegistryInjectionListener<T> rij;

    public RegistryModule(final TypeLiteral<Registry<T>> typeLiteral) {
        this.typeLiteral = typeLiteral;
        rawTClass = getRawType(typeLiteral);
        rij = new RegistryInjectionListener<T>();
    }

    @SuppressWarnings("unchecked")
    private Class<? super T> getRawType(final TypeLiteral<Registry<T>> typeLiteral) {
        final Type type = typeLiteral.getType();
        if(type instanceof ParameterizedType) {
            final Type arg = ((ParameterizedType) type).getActualTypeArguments()[0];
            return (Class<? super T>) arg;
        }
        throw new IllegalArgumentException("No discernible type parameters");
    }

    @Override
    protected void configure() {

        // bind the Registry<T> type to this module as a provider of it
        bind(typeLiteral).toProvider(this);

        bindListener(MoreMatchers.literalSubclassesOf(rawTClass), new RegistryTypeListener<T>(rij, rawTClass));
    }

    @Override
    public Registry<T> get() {
        return rij.getRegistry();
    }
}

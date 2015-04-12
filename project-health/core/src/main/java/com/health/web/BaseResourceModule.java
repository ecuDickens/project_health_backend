package com.health.web;


import com.google.inject.Scopes;
import com.health.exception.mappers.*;
import com.sun.jersey.guice.JerseyServletModule;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

public class BaseResourceModule extends JerseyServletModule {

    @Override
    protected void configureServlets() {
        bind(JacksonJaxbJsonProvider.class).in(Scopes.SINGLETON);
        bind(SafeJsonMappingExceptionMapper.class).in(Scopes.SINGLETON);
        bind(SafeJsonParseExceptionMapper.class).in(Scopes.SINGLETON);

        bind(HttpExceptionMapper.class).in(Scopes.SINGLETON);
        bind(IllegalArgumentExceptionMapper.class).in(Scopes.SINGLETON);
        bind(NoSuchElementExceptionMapper.class).in(Scopes.SINGLETON);
        bind(NullPointerExceptionMapper.class).in(Scopes.SINGLETON);
    }
}

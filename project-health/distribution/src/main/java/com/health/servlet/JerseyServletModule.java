package com.health.servlet;

import com.health.web.AccountResource;
import com.health.web.BaseResourceModule;
import com.google.inject.throwingproviders.ThrowingProviderBinder;
import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.ResourceFilters;

import java.util.HashMap;
import java.util.Map;

/**
 * Jersey Servlet Module
 */
public class JerseyServletModule extends BaseResourceModule {
    @Override
    protected void configureServlets() {
        super.configureServlets();

        bind(AccountResource.class);

        final Map<String, String> params = new HashMap<String, String>();
        params.put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE.toString());
        params.put(ResourceFilters.class.getName(), RolesAllowedResourceFilterFactory.class.getName());

        serve("/rest/*").with(GuiceContainer.class, params);
        filter("/*").through(DebuggingResponseFilter.class);

        // allow for @CheckProvides
        install(ThrowingProviderBinder.forModule(this));
    }
}

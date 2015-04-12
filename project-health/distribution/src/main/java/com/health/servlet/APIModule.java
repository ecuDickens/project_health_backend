package com.health.servlet;

import com.google.inject.AbstractModule;
import com.health.web.ProfileResource;

public class APIModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProfileResource.class);
    }
}

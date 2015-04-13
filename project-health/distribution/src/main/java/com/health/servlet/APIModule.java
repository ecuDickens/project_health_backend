package com.health.servlet;

import com.google.inject.AbstractModule;
import com.health.web.AccountResource;

public class APIModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountResource.class);
    }
}

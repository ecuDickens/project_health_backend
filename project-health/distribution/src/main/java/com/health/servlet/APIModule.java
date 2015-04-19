package com.health.servlet;

import com.google.inject.AbstractModule;
import com.health.web.AccountResource;
import com.health.web.ExerciseResource;
import com.health.web.LoginResource;
import com.health.web.LogoutResource;

public class APIModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountResource.class);
        bind(LoginResource.class);
        bind(LogoutResource.class);
        bind(ExerciseResource.class);
    }
}

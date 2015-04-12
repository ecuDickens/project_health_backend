package com.health.servlet;

import com.health.inject.DefaultConfigurationModule;
import com.health.inject.DefaultEnvironmentModule;
import com.health.jpa.JpaEntityManagerServiceImpl;
import com.health.jpa.JpaServiceConstants;
import com.health.jpa.spi.JpaEntityManagerService;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.health.service.threadpool.ThreadPoolServiceImpl;
import com.health.spi.ThreadPoolService;

/**
 * Module which defines all Service injections
 */
public class BaseServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DefaultEnvironmentModule());
        install(new DefaultConfigurationModule());
        bind(JpaEntityManagerService.class)
                .to(JpaEntityManagerServiceImpl.class)
                .in(Singleton.class);
        Multibinder.newSetBinder(binder(), String.class, Names.named(JpaServiceConstants.ENTITY_CLASS_NAMES));
        bind(ThreadPoolService.class)
                .to(ThreadPoolServiceImpl.class)
                .in(Singleton.class);
    }
}

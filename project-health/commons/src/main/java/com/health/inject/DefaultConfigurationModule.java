package com.health.inject;


import com.health.matchers.MoreMatchers;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.health.configuration.YamlConfigurationLoaderImpl;
import com.health.configuration.spi.ConfigurationLoader;

public class DefaultConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        bindListener(MoreMatchers.literalSubclassesOf(Configuration.class), new ConfigurationTypeListener());
        bind(ConfigurationLoader.class).to(YamlConfigurationLoaderImpl.class).in(Scopes.SINGLETON);
    }
}

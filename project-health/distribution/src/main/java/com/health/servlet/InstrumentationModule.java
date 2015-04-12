package com.health.servlet;

import com.google.inject.AbstractModule;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricsRegistry;

// modified from https://github.com/palominolabs/metrics-guice
public class InstrumentationModule extends AbstractModule {

    final MetricsRegistry metricsRegistry = Metrics.defaultRegistry();

    @Override
    protected void configure() {
        bind(MetricsRegistry.class).toInstance(metricsRegistry);
    }
}

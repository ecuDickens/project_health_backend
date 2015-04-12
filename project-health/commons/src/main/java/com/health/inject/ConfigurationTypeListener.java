package com.health.inject;


import java.lang.reflect.Type;
import java.util.List;

public class ConfigurationTypeListener extends GenericTypeListener<Configuration> {

    public ConfigurationTypeListener() {
        super(Configuration.class);
    }

    @Override
    public void afterInjection(Configuration injectee, List<Type> actualTypeArgs) {
        injectee.setType(actualTypeArgs.get(0));
    }
}

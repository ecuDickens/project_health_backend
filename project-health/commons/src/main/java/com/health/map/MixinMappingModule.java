package com.health.map;


import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.module.SimpleModule;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class MixinMappingModule extends SimpleModule {

    static final Version V1 = new Version(1, 0, 0, null);

    protected MixinMappingModule() {
        super(null, null);
    }

    @Override
    public String getModuleName() {
        return getClass().getCanonicalName();
    }

    @Override
    public Version version() {
        return V1;
    }
}

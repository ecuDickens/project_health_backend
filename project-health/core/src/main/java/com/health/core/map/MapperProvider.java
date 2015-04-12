package com.health.core.map;


import com.google.inject.Inject;
import com.google.inject.Provides;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.Set;

/**
 * Provider which returns an object mapper with default Jackson settings
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MapperProvider implements ContextResolver<ObjectMapper>, com.google.inject.Provider<ObjectMapper> {

    /**
     * A static instance of an object mapper with generally default settings
     * that does not take part in DI for configurable properties.
     */
    public static final ObjectMapper INSTANCE = new ObjectMapper();

    static {
        INSTANCE.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        INSTANCE.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
        INSTANCE.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        INSTANCE.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private final ObjectMapper objectMapper;

    /**
     * Inject the Set of Jackson modules in the DI context.
     */
    @Inject
    public MapperProvider(final Set<Module> mapperModules) {
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        objectMapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        for(Module mod : mapperModules) {
            objectMapper.registerModule(mod);
            INSTANCE.registerModule(mod);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }

    @Provides
    @Override
    public ObjectMapper get() {
        return objectMapper;
    }
}

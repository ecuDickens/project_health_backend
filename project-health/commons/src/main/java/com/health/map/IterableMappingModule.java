package com.health.map;


import org.codehaus.jackson.*;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Maps Iterable types to ArrayLists during deserialization.
 */
public class IterableMappingModule extends MixinMappingModule {

    public IterableMappingModule() {
        addSerializer(Iterable.class, new IterableSerializer());
        addDeserializer(Iterable.class, new IterableDeserializer());
    }

    public static class IterableSerializer extends JsonSerializer<Iterable> {

        @Override
        public Class<Iterable> handledType() {
            return Iterable.class;
        }

        @Override
        public void serialize(Iterable iterable, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeStartArray();
            for(Object o : iterable) {
                provider.defaultSerializeValue(o, jgen);
            }
            jgen.writeEndArray();
        }

        @Override
        public void serializeWithType(Iterable iterable, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            typeSer.writeTypePrefixForScalar(iterable, jgen);
            serialize(iterable, jgen, provider);
            typeSer.writeTypeSuffixForScalar(iterable, jgen);
        }
    }

    public static class IterableDeserializer extends JsonDeserializer<Iterable<?>> implements ContextualDeserializer<Iterable<?>> {

        private static final JavaType defaultType = TypeFactory.defaultInstance().constructType(Object.class);

        JavaType containedType;

        public IterableDeserializer() {
            this(defaultType);
        }

        public IterableDeserializer(JavaType containedType) {
            this.containedType = containedType;
        }

        void assertCurrentToken(JsonToken expected, JsonParser jp, DeserializationContext ctxt) throws JsonMappingException {
            if(!expected.equals(jp.getCurrentToken())) {
                throw ctxt.wrongTokenException(jp, expected, "Invalid content");
            }
        }

        @Override
        public Iterable<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            final List<Object> list = new ArrayList<Object>();

            assertCurrentToken(JsonToken.START_ARRAY, jp, ctxt);

            final ObjectCodec codec = jp.getCodec();
            final Iterator<?> iter = codec.readValues(jp, containedType);
            while(iter.hasNext()) {
                list.add(iter.next());
            }

            return list;
        }

        @Override
        public JsonDeserializer<Iterable<?>> createContextual(DeserializationConfig config, BeanProperty property) throws JsonMappingException {
            final JsonDeserializer<Iterable<?>> deserializer;
            JavaType type = null != property ? property.getType() : defaultType;
            JavaType containedType = type.containedType(0);
            if(containedType != null) {
                deserializer = buildTypedDeserializer(containedType);
            }
            else {
                deserializer = this;
            }
            return deserializer;
        }

        protected IterableDeserializer buildTypedDeserializer(JavaType containedType) {
            return new IterableDeserializer(containedType);
        }
    }
}

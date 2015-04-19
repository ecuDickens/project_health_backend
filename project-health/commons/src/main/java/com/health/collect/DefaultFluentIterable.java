package com.health.collect;


import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.health.map.IterableMappingModule;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * A default implementation of FluentIterable.
 */
@JsonDeserialize(using = DefaultFluentIterable.DefaultFluentIterableDeserializer.class)
public class DefaultFluentIterable<T> implements FluentIterable<T> {

    private final Iterable<T> delegate;

    public DefaultFluentIterable(Iterable<T> delegate) {
        Preconditions.checkArgument(null != delegate, "delegate must not be null");
        this.delegate = delegate;
    }

    private static <T> FluentIterable<T> from(Iterable<T> source) {
        if(source instanceof FluentIterable) {
            return (FluentIterable<T>)source;
        }
        else {
            return new DefaultFluentIterable<T>(source);
        }
    }

    @Override
    public int size() {
        return Iterables.size(delegate);
    }

    @Override
    public T get(int position) {
        return Iterables.get(delegate, position);
    }

    @Override
    public Optional<T> first() {
        // get the iterator only once
        final Iterator<T> iterator = delegate.iterator();
        return iterator.hasNext() ?
                Optional.of(iterator.next()) :
                Optional.<T>absent();
    }

    @Override
    public boolean any(Predicate<? super T> pred) {
        return Iterables.any(delegate, pred);
    }

    @Override
    public boolean all(Predicate<? super T> pred) {
        return Iterables.all(delegate, pred);
    }

    @Override
    public <K> ListMultimap<K, T> index(Function<? super T, K> keyFn) {
        final ListMultimap<K, T> multimap = ArrayListMultimap.create();
        for(T value : delegate) {
            multimap.put(keyFn.apply(value), value);
        }
        return multimap;
    }

    @Override
    public FluentIterable<T> each(Function<? super T, ?> fn) {
        for(T t : delegate) {
            fn.apply(t);
        }
        return this;
    }

    @Override
    public <R> FluentIterable<R> transform(Function<FluentIterable<T>, Iterable<R>> fn) {
        return from(fn.apply(this));
    }

    @Override
    public <R> FluentIterable<R> map(Function<? super T, ? extends R> fn) {
        return from(Iterables.transform(delegate, fn));
    }

    @Override
    public FluentIterable<T> limit(int limitSize) {
        return from(Iterables.limit(delegate, limitSize));
    }

    @Override
    public FluentIterable<T> sub(int start, int end) {
        Preconditions.checkArgument(start >= 0, "Start index must be zero or positive");
        Preconditions.checkArgument(end > start, "End index must be greater than start index");
        return from(Iterables.limit(Iterables.skip(delegate, start), end - start));
    }

    @Override
    public List<T> toList() {
        return Lists.newArrayList(delegate);
    }

    @Override
    public PeekingIterator<T> iterator() {
        return MoreIterators.withPeeking(delegate.iterator());
    }

    @Override
    public String toString() {
        return "DefaultFluentIterable{" +
                "delegate=" + delegate +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DefaultFluentIterable)) {
            return false;
        }
        DefaultFluentIterable other = (DefaultFluentIterable)obj;
        return delegate.equals(other.delegate);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 13).appendSuper(delegate.hashCode()).build();
    }


    public static class DefaultFluentIterableDeserializer extends IterableMappingModule.IterableDeserializer {
        DefaultFluentIterableDeserializer(JavaType containedType) {
            super(containedType);
        }

        @Override
        public Iterable<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new DefaultFluentIterable(super.deserialize(jp, ctxt));
        }

        @Override
        protected IterableMappingModule.IterableDeserializer buildTypedDeserializer(JavaType containedType) {
            return new DefaultFluentIterableDeserializer(containedType);
        }
    }
}

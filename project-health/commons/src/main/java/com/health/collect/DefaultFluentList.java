package com.health.collect;


import com.google.common.base.Preconditions;
import com.health.map.IterableMappingModule;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import static com.google.common.collect.Lists.newArrayList;

/**
 * An extension of DefaultFluentIterable that allows for functional optimizations
 * using List APIs by implementing List directly.
 */
@JsonDeserialize(using = DefaultFluentList.DefaultFluentListDeserializer.class)
public class DefaultFluentList<T> extends DefaultFluentIterable<T> implements List<T> {

    private final List<T> delegate;

    public DefaultFluentList(List<T> delegate) {
        super(delegate);
        Preconditions.checkArgument(null != delegate, "delegate must not be null");
        this.delegate = delegate;
    }

    public boolean add(T t) {
        return delegate.add(t);
    }

    public void add(int index, T element) {
        delegate.add(index, element);
    }

    public boolean addAll(Collection<? extends T> c) {
        return delegate.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return delegate.addAll(index, c);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean contains(Object element) {
        return delegate.contains(element);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public T get(int index) {
        return delegate.get(index);
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DefaultFluentList)) {
            return false;
        }
        DefaultFluentList other = (DefaultFluentList)obj;
        return delegate.equals(other.delegate);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(73, 5).appendSuper(delegate.hashCode()).build();
    }

    @Override
    public int indexOf(Object o) {
        return delegate.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public int lastIndexOf(Object o) {
        return delegate.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return delegate.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return delegate.listIterator(index);
    }

    @Override
    public T remove(int index) {
        return delegate.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        return delegate.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return delegate.retainAll(c);
    }

    public T set(int index, T element) {
        return delegate.set(index, element);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return delegate.subList(fromIndex, toIndex);
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <R extends Object> R[] toArray(R[] a) {
        return delegate.toArray(a);
    }

    public static class DefaultFluentListDeserializer extends IterableMappingModule.IterableDeserializer {

        @Override
        public Iterable<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            final Iterable<?> iterable = super.deserialize(jp, ctxt);
            final List<?> list;
            if(iterable instanceof List) {
                list = (List)iterable;
            }
            else {
                list = newArrayList(iterable);
            }
            return new DefaultFluentList(list);
        }

        @Override
        protected IterableMappingModule.IterableDeserializer buildTypedDeserializer(JavaType containedType) {
            return new DefaultFluentIterableDeserializer(containedType);
        }
    }
}

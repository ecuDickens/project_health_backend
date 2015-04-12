package com.health.collect;

import com.google.common.collect.Iterators;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MoreIterables {

    public static <T> Iterable<T> emptyIterable() {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.emptyIterator();
            }
        };
    }

    public static <T> Iterable<T> nullToEmpty(Iterable<T> iterable) {
        return (null != iterable) ? iterable : MoreIterables.<T>emptyIterable();
    }

    public static Iterable<String> toString(final Iterable<?> iterable) {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return MoreIterators.toString(iterable.iterator());
            }
        };
    }

    /**
     * Returns a FluentIterable wrapper around the given iterable. This supports
     * null iterable values by returning an empty iterable to allow for safe
     * handling without the need to check beforehand.
     */
    public static <T> FluentIterable<T> asFluent(final Iterable<T> iterable) {
        if (iterable instanceof FluentIterable) {
            return (FluentIterable<T>) iterable;
        } else if (iterable instanceof List) {
            return new DefaultFluentList<T>((List<T>) iterable);
        } else {
            // transform a null to an empty iterable
            return new DefaultFluentIterable<T>(nullToEmpty(iterable));
        }
    }

    /**
     * Returns a FluentIterable wrapper around the given array.
     */
    public static <T> FluentIterable<T> asFluent(final T[] array) {
        return asFluent(null != array ? Arrays.asList(array) : null);
    }
}
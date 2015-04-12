package com.health.collect;

import com.google.common.base.Functions;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.Iterator;

public class MoreIterators {

    public static <T> PeekingIterator<T> withPeeking(final Iterator<T> iterator) {
        return iterator instanceof PeekingIterator ? (PeekingIterator<T>) iterator :
                new PeekingAbstractIterator<T>() {
                    @Override
                    protected T computeNext() {
                        if (iterator.hasNext()) {
                            return iterator.next();
                        }
                        return endOfData();
                    }
                };
    }

    public static Iterator<String> toString(Iterator<?> iterator) {
        return Iterators.transform(iterator, Functions.toStringFunction());
    }
}

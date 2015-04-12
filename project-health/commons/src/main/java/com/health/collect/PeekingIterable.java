package com.health.collect;

import com.google.common.collect.PeekingIterator;

/**
 * An iterable that returns a PeekingIterator.
 */
public interface PeekingIterable<T> extends Iterable<T> {

    @Override
    PeekingIterator<T> iterator();
}

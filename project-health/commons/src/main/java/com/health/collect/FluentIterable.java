package com.health.collect;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ListMultimap;

import java.util.List;

/**
 * An iterable with fluent-apis for manipulating and interacting
 * with directly.
 */
public interface FluentIterable<T> extends PeekingIterable<T> {

    /**
     * Returns the number of elements in the FluentIterable.
     */
    int size();

    /**
     * Returns the element at position.
     */
    T get(int position);

    /**
     * Returns an optional that contains the first element
     * in this iterable. If this iterable is empty, this
     * returns Optional.absent().
     */
    Optional<T> first();

    /**
     * Returns true if the predicate returns true for any element
     * within this iterable.
     */
    boolean any(Predicate<? super T> pred);

    /**
     * Returns true if the predicate does not return false for
     * any element within this iterable. Therefore this will
     * return true if this iterable is empty.
     */
    boolean all(Predicate<? super T> pred);

    /**
     * Creates an ListMultimap by iterating over the
     * elements in this iterable, applying the keyFn to the
     * element to determine its key, and putting that element
     * into the multimap at the produced key from the keyFn.
     */
    <K> ListMultimap<K, T> index(Function<? super T, K> keyFn);

    /**
     * Alternative way of iterating this iterable by supplying
     * a function argument to call for each element in this iterable.
     * Convenient short-hand for reusing existing functions.
     */
    FluentIterable<T> each(Function<? super T, ?> fn);

    /**
     * Returns a FluentIterable which is a generalized transform over
     * this FluentIterable itself by applying fn with this FluentIterable
     * as the argument and converting its return value to a FluentIterable.
     * This provides a mechanism for higher-level transformations using
     * reusable Functions and/or composed.
     */
    <R> FluentIterable<R> transform(Function<FluentIterable<T>, Iterable<R>> fn);

    /**
     * Returns a FluentIterable whose contents are the result of
     * a transformation over the elements of this iterable.
     * The result of applying fn to each element in the source
     * iterable is used as the corresponding element in the
     * returned FluentIterable.
     */
    <R> FluentIterable<R> map(Function<? super T, ? extends R> fn);

    /**
     * Returns a FluentIterable that contains no more
     * than limitSize elements of this iterable.
     */
    FluentIterable<T> limit(int limitSize);

    /**
     * Returns a FluentIterable that skips elements
     * up to the start index and ends at the end index.
     */
    FluentIterable<T> sub(int start, int end);

    /**
     * Returns a modifiable list containing all the elements in this
     * iterable in order.
     */
    List<T> toList();
}

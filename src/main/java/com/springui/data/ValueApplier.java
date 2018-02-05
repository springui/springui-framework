package com.springui.data;

/**
 * @author Stephan Grundner
 */
public interface ValueApplier<T, V> {

    void apply(T t, V value);
}

package com.springui.data;

/**
 * @author Stephan Grundner
 */
public interface ValueWriter<T, V> {

    void write(T t, V value);
}

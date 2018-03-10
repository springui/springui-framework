package com.springui.data;

/**
 * @author Stephan Grundner
 */
public interface ValueReader<T, V> {

    V read(T o);
}

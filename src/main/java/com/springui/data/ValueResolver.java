package com.springui.data;

/**
 * @author Stephan Grundner
 */
public interface ValueResolver<T, V> {

    V resolve(T o);
}

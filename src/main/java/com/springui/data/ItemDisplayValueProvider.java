package com.springui.data;

/**
 * @author Stephan Grundner
 */
public interface ItemDisplayValueProvider<T> {

    String getDisplayValue(T t);
}

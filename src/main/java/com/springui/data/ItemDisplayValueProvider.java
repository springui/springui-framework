package com.springui.data;

/**
 * @author Stephan Grundner
 */
@Deprecated
public interface ItemDisplayValueProvider<T> {

    String getDisplayValue(T t);
}

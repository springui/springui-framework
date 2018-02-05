package com.springui.data;

/**
 * @author Stephan Grundner
 */
public final class ToStringDisplayValueProvider<T> implements ItemDisplayValueProvider<T> {

    @Override
    public String getDisplayValue(T t) {
        if (t != null) {
            return t.toString();
        }

        return null;
    }
}

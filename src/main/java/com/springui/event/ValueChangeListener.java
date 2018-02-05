package com.springui.event;

/**
 * @author Stephan Grundner
 */
public interface ValueChangeListener<T> {

    void valueChanged(ValueChange<T> change);
}

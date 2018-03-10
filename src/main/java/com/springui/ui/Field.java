package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public interface Field<T> extends Component {

    T getValue();
    void setValue(T value);
}

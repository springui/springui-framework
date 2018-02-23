package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public class BooleanField extends Field<Boolean> {

    private Boolean value;

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    protected void valueChanged(Boolean newValue, Boolean oldValue) {
        value = newValue;
    }
}

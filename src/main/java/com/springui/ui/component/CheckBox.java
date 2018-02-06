package com.springui.ui.component;

/**
 * @author Stephan Grundner
 */
public class CheckBox extends Field<Boolean> {

    {
        setTemplate("ui/checkbox");
    }

    private Boolean value;

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    protected void valueChanged(Boolean newValue, Boolean oldValue) {
        value = newValue;
    }

    public CheckBox() { }
}

package com.springui.ui.component;

/**
 * @author Stephan Grundner
 */
public class TextField extends Field<String> {

    {
        setTemplate("ui/text-field");
    }

    private String value;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    protected void valueChanged(String newValue, String oldValue) {
        value = newValue;
    }

    public TextField() { }
}

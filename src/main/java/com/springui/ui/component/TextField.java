package com.springui.ui.component;

import com.springui.ui.Template;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/text-field")
public class TextField extends Field<String> {

    private String value;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    protected void valueChanged(String newValue, String oldValue) {
        value = newValue;
    }
}

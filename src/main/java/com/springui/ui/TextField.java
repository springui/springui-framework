package com.springui.ui;

import com.springui.i18n.Message;

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

    public TextField(Message caption) {
        super(caption);
    }

    public TextField() { }
}

package com.springui.ui;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/text-field")
public class TextField extends AbstractField<String> {

    private String value;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    protected void changeValue(String value) {
        this.value = value;
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        visitor.visit(this);
    }
}

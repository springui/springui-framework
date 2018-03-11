package com.springui.ui;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/progress")
public class Progress extends AbstractComponent {

    private float value;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        visitor.visit(this);
    }
}

package com.springui.ui;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/overlay")
public class Overlay extends AbstractSingleComponentContainer {

    private final Button close = new Button();

    public Button getClose() {
        return close;
    }

    @Override
    public void setParent(Component parent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        close.walk(visitor);
        super.walk(visitor);
    }

    public Overlay() {
        close.addActionListener(new ActionListener() {
            @Override
            public void perform(Action action) {
                action.getRequest().getUi().removeOverlay(Overlay.this);
            }
        });
    }
}

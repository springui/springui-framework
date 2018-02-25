package com.springui.ui;

import com.springui.event.Action;
import com.springui.event.ActionListener;

/**
 * @author Stephan Grundner
 */
public class Overlay extends SingleComponentLayout {

    private Trigger close;

    public Trigger getClose() {
        return close;
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        super.walk(visitor);
        close.walk(visitor);
    }

    public Overlay() {
        close = new Trigger();
        close.addActionListener(new ActionListener() {
            @Override
            public void performAction(Action action) {
                getUi().hideOverlay(Overlay.this);
            }
        });
    }
}

package com.springui.ui;

import com.springui.i18n.Message;

/**
 * @author Stephan Grundner
 */
public class ConfirmationOverlay extends Overlay {

    private ComponentsMapLayout layout;

    private final Button confirm = new Button();
    private final Button decline = new Button();

    public Button getConfirm() {
        return confirm;
    }

    public Button getDecline() {
        return decline;
    }

    public ConfirmationOverlay() {
        layout = new ComponentsMapLayout("{theme}/confirmation");

        confirm.setCaption(new Message("ui.confirmation.confirm", "OK"));
        layout.addComponent("confirm", confirm);

        decline.setCaption(new Message("ui.confirmation.decline", "Decline"));
        layout.addComponent("decline", decline);

        Button close = getClose();
        close.setCaption(new Message("ui.confirmation.close", "Close"));
        layout.addComponent("close", close);

        setContent(layout);
    }
}

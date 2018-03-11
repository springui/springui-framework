package com.springui.ui;

import com.springui.i18n.Message;
import com.springui.ui.layout.CustomLayout;

/**
 * @author Stephan Grundner
 */
public class ConfirmationOverlay extends Overlay {

    private CustomLayout layout;

    private Message message;

    private final Button confirm = new Button();
    private final Button decline = new Button();

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Button getConfirm() {
        return confirm;
    }

    public Button getDecline() {
        return decline;
    }

    public ConfirmationOverlay() {
        layout = new CustomLayout("{theme}/confirmation");

        confirm.setCaption(new Message("ui.confirmation.confirm", "OK"));
        confirm.setColor(Color.SUCCESS);
        layout.addComponent("confirm", confirm);

        decline.setCaption(new Message("ui.confirmation.decline", "Decline"));
        decline.setColor(Color.DANGER);
        layout.addComponent("decline", decline);

        Button close = getClose();
        close.setCaption(new Message("ui.confirmation.close", "Close"));
        close.setColor(Color.SECONDARY);
        layout.addComponent("close", close);

        setContent(layout);
    }
}

package com.springui.ui;

import com.springui.i18n.Message;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/text")
public class Text extends AbstractComponent {

    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        visitor.visit(this);
    }
}

package com.springui.ui;

import com.springui.event.ActionListener;
import com.springui.i18n.Message;

/**
 * @author Stephan Grundner
 */
public class Trigger extends AbstractComponent {

    public Trigger(Message caption, ActionListener actionListener) {
        setCaption(caption);
        addActionListener(actionListener);
    }

    public Trigger(String messageCode, String defaultMessage, ActionListener actionListener) {
        this(new Message(messageCode, defaultMessage), actionListener);
    }

    @Deprecated
    public Trigger(String defaultMessage, ActionListener actionListener) {
        this(new Message((String) null, defaultMessage), actionListener);
    }

    public Trigger() { }
}

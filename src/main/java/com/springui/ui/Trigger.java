package com.springui.ui;

import com.springui.event.ActionListener;
import com.springui.i18n.Message;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/button")
public class Trigger extends Component {

    public Trigger(Message caption, ActionListener actionListener) {
        setCaption(caption);
        addActionListener(actionListener);
    }

    public Trigger(String defaultMessage, ActionListener actionListener) {
        this(new Message(defaultMessage), actionListener);
    }

    public Trigger() { }
}

package com.springui.ui;

import com.springui.event.ActionListener;
import com.springui.i18n.Message;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/button")
public class Trigger extends Component {

    public Trigger() { }

    public Trigger(Text caption) {
        setCaption(caption);
    }

    public Trigger(Message caption, ActionListener actionListener) {
        this(new Text(caption));
        addActionListener(actionListener);
    }

    @Deprecated
    public Trigger(String caption, ActionListener actionListener) {
        this(new Text(caption));
        addActionListener(actionListener);
    }
}

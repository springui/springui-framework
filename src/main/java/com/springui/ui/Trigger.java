package com.springui.ui;

import com.springui.event.ActionListener;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/button")
public class Trigger extends Component {

    public Trigger() { }

    public Trigger(Text caption) {
        setCaption(caption);
    }

    @Deprecated
    public Trigger(String caption, ActionListener actionListener) {
        this(new Text(caption));
        addActionListener(actionListener);
    }
}

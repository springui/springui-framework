package com.springui.ui.component;

import com.springui.event.ActionListener;

/**
 * @author Stephan Grundner
 */
public class Button extends Component {

    {
        setTemplate("ui/button");
    }

    public Button() { }

    public Button(Text caption) {
        setCaption(caption);
    }

    @Deprecated
    public Button(String caption, ActionListener actionListener) {
        this(new Text(caption));
        addActionListener("", actionListener);
    }
}

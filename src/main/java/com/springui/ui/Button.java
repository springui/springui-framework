package com.springui.ui;

import com.springui.i18n.Message;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/button")
public class Button extends AbstractComponent {

    @Override
    public void walk(ComponentVisitor visitor) {
        visitor.visit(this);
    }

    public Button(Message caption, ActionListener actionListener) {
        setCaption(caption);
        addActionListener(actionListener);
    }

    public Button(String captionCode, String defaultCaption, ActionListener actionListener) {
        this(new Message(captionCode, defaultCaption), actionListener);
    }

    public Button() { }
}

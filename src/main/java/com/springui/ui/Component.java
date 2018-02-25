package com.springui.ui;

import com.springui.event.Action;
import com.springui.event.ActionListener;
import com.springui.i18n.Message;

import java.util.Set;

/**
 * @author Stephan Grundner
 */
public interface Component {

    String getId();
    void setId(String id);

    UI getUi();
    void setUi(UI ui);

    void walk(ComponentVisitor visitor);

    Component getParent();
    void setParent(Component parent);

    boolean isDisabled();
    void setDisabled(boolean disabled);

    Message getCaption();
    void setCaption(Message caption);

    Set<ActionListener> getActionListeners();
    void addActionListener(ActionListener actionListener);
    void performAction(Action action);
}

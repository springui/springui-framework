package com.springui.ui.component;

import com.springui.Message;
import com.springui.event.Action;
import com.springui.event.ActionListener;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Stephan Grundner
 */
public abstract class Component {

    private String id;
    private String template;
    private Component parent;

    private Text caption;

    private Message message;

    private final Set<ActionListener> actionListeners = new LinkedHashSet<>();

    public String getId() {
        if (StringUtils.isEmpty(id)) {
            id = UUID.randomUUID().toString();
        }

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public UI getUi() {
        return UI.getCurrent();
    }

    protected Component getRoot() {
        if (parent != null) {
            return parent.getRoot();
        } else {
            return this;
        }
    }

    protected void walk(ComponentVisitor visitor) {
        visitor.visit(this);
    }

    public Component getParent() {
        return parent;
    }

    protected void setParent(Component parent) {
        this.parent = parent;

        Component root = getRoot();
        if (root instanceof UI) {
            walk(getUi()::attach);
        } else {
            walk(getUi()::detach);
        }
    }

    protected void attached() {}

    protected void detached() {}

    public final boolean isAttached() {
        UI ui = getUi();
        if (ui != null) {
            return ui.getComponent(id) == this;
        }

        return false;
    }

    public Text getCaption() {
        return caption;
    }

    public void setCaption(Text caption) {
        this.caption = caption;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Set<ActionListener> getActionListeners() {
        return Collections.unmodifiableSet(actionListeners);
    }

    public void addActionListener(String event, ActionListener actionListener) {
        actionListeners.add(actionListener);
    }

    public void performAction(Action action) {
        for (ActionListener actionListener : actionListeners) {
            actionListener.onAction(action);
        }
    }
}

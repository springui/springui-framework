package com.springui.ui;

import com.springui.event.Action;
import com.springui.event.ActionListener;
import com.springui.i18n.Message;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractComponent implements Component {

    private String id;
    private Component parent;
    private boolean disabled;
    private Message caption;

    private UI ui;

    private final Set<ActionListener> actionListeners = new LinkedHashSet<>();

    @Override
    public String getId() {
        if (StringUtils.isEmpty(id)) {
            id = UUID.randomUUID().toString();
        }

        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public UI getUi() {
        return ui;
    }

    @Override
    public void setUi(UI ui) {
        this.ui = ui;
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Component getParent() {
        return parent;
    }

    private void parentChanged(Component currentParent, Component newParent) {
        if (currentParent != null) {

            if (newParent != null) {
                throw new IllegalStateException(toString() + " already has a parent.");
            }

        }
    }

    private UI findUi() {
        if (ui == null && parent != null) {
            return parent.getUi();
        }

        return ui;
    }

    @Override
    public void setParent(Component parent) {
        if (this.parent != parent) {
            parentChanged(this.parent, parent);
        }

        this.parent = parent;

        if (parent == null) {
            if (ui != null) {
                ui.detach(this);
            }
        } else {
            UI ui = findUi();
            if (ui != null) {
                ui.attach(this);
            }
        }
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public Message getCaption() {
        return caption;
    }

    @Override
    public void setCaption(Message caption) {
        this.caption = caption;
    }

    @Override
    public final Set<ActionListener> getActionListeners() {
        return Collections.unmodifiableSet(actionListeners);
    }

    @Override
    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }

    public void addActionListener(String event, ActionListener actionListener) {
        actionListeners.add(actionListener);
    }

    @Override
    public void performAction(Action action) {
        actionListeners.forEach(it -> it.performAction(action));
    }
}

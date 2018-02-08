package com.springui.ui;

import com.springui.i18n.Message;
import com.springui.event.Action;
import com.springui.event.ActionListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author Stephan Grundner
 */
public abstract class Component {

    private String id;
    private String template;
    private Component parent;

    private boolean disabled;
    private int tabIndex;
    private Text caption;
    private Message message;

    private final Set<String> styles = new LinkedHashSet<>();

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
        if (StringUtils.isEmpty(template)) {
            Template annotation = AnnotationUtils
                    .findAnnotation(this.getClass(), Template.class);
            return (String) AnnotationUtils.getValue(annotation, "name");
        }

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

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
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

    public Set<String> getStyles() {
        return Collections.unmodifiableSet(styles);
    }

    public boolean addStyle(String style) {
        return styles.add(style);
    }

    public boolean removeStyle(String style) {
        return styles.remove(style);
    }

    public Set<ActionListener> getActionListeners() {
        return Collections.unmodifiableSet(actionListeners);
    }

    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }

    public void performAction(Action action) {
        for (ActionListener actionListener : actionListeners) {
            actionListener.performAction(action);
        }
    }
}

package com.springui.ui;

import com.springui.event.Action;
import com.springui.event.ActionListener;
import com.springui.i18n.Message;
import com.springui.web.TemplateProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Stephan Grundner
 */
public abstract class Component implements TemplateProvider {

    private String id;
    private String template;
    private Component parent;

    private boolean disabled;
    private int tabIndex;
    private Message caption;
    private Message message;

    private UI ui;

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
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public UI getUi() {
        return ui;
    }

    protected void setUi(UI ui) {
        this.ui = ui;
    }

    private UI findUi() {
        Component component = this;
        while (component != null) {
            UI ui = component.ui;
            if (ui != null) {
                return ui;
            }
            component = component.parent;
        }

        return null;
    }

    protected void walk(ComponentVisitor visitor) {
        visitor.visit(this);
    }

    public Component getParent() {
        return parent;
    }

    protected void setParent(Component parent) {
        this.parent = parent;

        if (parent != null) {
            UI ui = findUi();
            if (ui != null) {
                ui.attach(this);
            }
        } else {
            if (ui != null) {
                ui.detach(this);
            }
        }
    }

    public boolean isAttached() {
        return getUi() != null;
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

    public Message getCaption() {
        return caption;
    }

    public void setCaption(Message caption) {
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

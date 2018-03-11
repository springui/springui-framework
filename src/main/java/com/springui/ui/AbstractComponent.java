package com.springui.ui;

import com.springui.i18n.Message;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractComponent implements Component {

    private String id;
    private UI ui;
    private Component parent;

    private Message caption;

    private final Set<ActionListener> actionListeners = new LinkedHashSet<>();

    private Color color;
    private Size size = Size.NORMAL;

    @Override
    public String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }

        return id;
    }

    public UI getUi() {
        return ui;
    }

    public void setUi(UI ui) {
        if (ui != null) {
            if (!ui.getComponents().containsKey(getId())) {
                throw new IllegalStateException();
            }
        }

        this.ui = ui;
    }

    @Override
    public Component getParent() {
        return parent;
    }

    protected Component findRoot() {
        Component component = this;
        do {
            Component parent = component.getParent();
            if (parent == null) {
                return component;
            }
            component = parent;
        } while (true);
    }

    @Override
    public void setParent(Component parent) {
        this.parent = parent;

        Component root = findRoot();
        if (root instanceof UI) {
            ((UI) root).attach(this);
        } else {
            UI ui = getUi();
            if (ui != null) {
                ui.detach(this);
            }
        }
    }

    public Message getCaption() {
        return caption;
    }

    public void setCaption(Message caption) {
        this.caption = caption;
    }

    @Override
    public Set<ActionListener> getActionListeners() {
        return Collections.unmodifiableSet(actionListeners);
    }

    @Override
    public boolean addActionListener(ActionListener actionListener) {
        return actionListeners.add(actionListener);
    }

    @Override
    public boolean removeActionListener(ActionListener actionListener) {
        return actionListeners.remove(actionListener);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public void setSize(Size size) {
        this.size = size;
    }
}

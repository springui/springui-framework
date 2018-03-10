package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public class AbstractSingleComponentContainer extends AbstractComponentsContainer implements SingleComponentContainer {

    private Component content;

    @Override
    public Component getContent() {
        return content;
    }

    @Override
    public void setContent(Component content) {
        this.content = content;

        content.setParent(this);
    }

    @Override
    public final void addComponent(Component component) {
        setContent(component);
    }

    @Override
    public void removeComponent(Component component) {
        component.setParent(null);

        this.content = null;
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        visitor.visit(this);
        content.walk(visitor);
    }
}

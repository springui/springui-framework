package com.springui.ui;

import java.util.Collections;
import java.util.Iterator;

/**
 * @author Stephan Grundner
 */
public class SingleComponentContainer extends ComponentsContainer {

    private Component component;

    @Override
    public final Iterator<Component> iterator() {
        if (component == null) {
            return Collections.emptyIterator();
        }

        return Collections.singleton(component)
                .iterator();
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;

        if (component != null) {
            component.setParent(this);
        }
    }

    @Override
    protected final void addComponent(Component component) {
        setComponent(component);
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        super.walk(visitor);
        if (component != null) {
            component.walk(visitor);
        }
    }
}

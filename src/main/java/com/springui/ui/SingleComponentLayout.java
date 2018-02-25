package com.springui.ui;

import java.util.Collections;
import java.util.Iterator;

/**
 * @author Stephan Grundner
 */
public class SingleComponentLayout extends AbstractComponentsContainer implements SingleComponentContainer {

    private Component component;

    @Override
    public final Iterator<Component> iterator() {
        if (component == null) {
            return Collections.emptyIterator();
        }

        return Collections.singleton(component)
                .iterator();
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public void setComponent(AbstractComponent component) {
        this.component = component;

        if (component != null) {
            component.setParent(this);
        }
    }

    @Override
    public final void addComponent(AbstractComponent component) {
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

package com.springui.ui.component;

import java.util.Collections;
import java.util.Iterator;

/**
 * @author Stephan Grundner
 */
public abstract class SingleComponentContainer<C extends Component> extends ComponentsContainer<C> {

    private C component;

    @Override
    public Iterator<C> iterator() {
        if (component == null) {
            return Collections.emptyIterator();
        }

        return Collections.singleton(component)
                .iterator();
    }

    public C getComponent() {
        return component;
    }

    public void setComponent(C component) {
        this.component = component;

        if (component != null) {
            component.setParent(this);
        }
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        super.walk(visitor);
        if (component != null) {
            component.walk(visitor);
        }
    }
}

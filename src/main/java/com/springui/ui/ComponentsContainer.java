package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public abstract class ComponentsContainer<C extends Component> extends Component implements Iterable<C> {

    protected abstract void addComponent(C component);

    @Override
    public void walk(ComponentVisitor visitor) {
        super.walk(visitor);
        for (Component component : this) {
            visitor.visit(component);
        }
    }
}

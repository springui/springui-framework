package com.springui.ui.component;

/**
 * @author Stephan Grundner
 */
public abstract class ComponentsContainer<C extends Component> extends Component implements Iterable<C> {

    @Override
    public void walk(ComponentVisitor visitor) {
        super.walk(visitor);
        for (Component component : this) {
            visitor.visit(component);
        }
    }
}

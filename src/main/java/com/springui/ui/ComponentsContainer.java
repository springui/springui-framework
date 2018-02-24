package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public abstract class ComponentsContainer extends Component implements Iterable<Component> {

    protected abstract void addComponent(Component component);

    @Override
    public void walk(ComponentVisitor visitor) {
        super.walk(visitor);
        for (Component component : this) {
            visitor.visit(component);
        }
    }
}

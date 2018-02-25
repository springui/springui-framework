package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractComponentsContainer extends AbstractComponent implements ComponentsContainer {

    @Override
    public void walk(ComponentVisitor visitor) {
        super.walk(visitor);
        for (Component component : this) {
            visitor.visit(component);
        }
    }
}

package com.springui.ui;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/form-layout")
public class FormLayout extends AbstractComponentsContainer {

    private Set<Component> components = new LinkedHashSet<>();

    @Override
    public void addComponent(Component component) {
        if (components.add(component)) {

        }
    }

    @Override
    public void removeComponent(Component component) {
        if (components.remove(component)) {

        }
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        visitor.visit(this);
        components.forEach(visitor::visit);
    }
}

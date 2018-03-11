package com.springui.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/form-layout")
public class FormLayout extends AbstractComponentsContainer {

    private Set<Component> components = new LinkedHashSet<>();

    public Collection<Component> getComponents() {
        return Collections.unmodifiableCollection(components);
    }

    @Override
    public void addComponent(Component component) {
        if (components.add(component)) {
            component.setParent(this);
        }
    }

    @Override
    public void removeComponent(Component component) {
        if (components.remove(component)) {
            component.setParent(null);
        }
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        visitor.visit(this);
        components.forEach(visitor::visit);
    }
}

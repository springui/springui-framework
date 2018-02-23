package com.springui.ui;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
public class FormLayout extends ComponentsContainer<Component> implements Layout {

    private final Set<Component> components = new LinkedHashSet<>();

    public Set<Component> getComponents() {
        return Collections.unmodifiableSet(components);
    }

    @Override
    public void addComponent(Component component) {
        components.add(component);
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        super.walk(visitor);
        components.forEach(visitor::visit);
    }
}

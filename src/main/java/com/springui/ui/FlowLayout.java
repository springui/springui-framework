package com.springui.ui;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
public class FlowLayout extends AbstractComponentsContainer {

    private Set<Component> components = new LinkedHashSet<>();

    @Override
    public void addComponent(Component component) {
        if (components.add(component)) {
            component.setParent(this);
        }
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }
}

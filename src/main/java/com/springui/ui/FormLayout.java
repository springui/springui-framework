package com.springui.ui;

import java.util.Iterator;

/**
 * @author Stephan Grundner
 */
public class FormLayout extends ComponentsContainer<Component> implements Layout {

    protected static class ComponentContainer extends SingleComponentContainer<Component> {

    }

    @Override
    public void addComponent(Component component) { }

    @Override
    public Iterator<Component> iterator() {
        return null;
    }
}

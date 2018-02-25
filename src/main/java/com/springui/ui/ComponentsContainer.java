package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public interface ComponentsContainer extends Component, Iterable<Component> {

    void addComponent(Component component);
}

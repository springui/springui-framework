package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public interface SingleComponentContainer extends ComponentsContainer {

    Component getComponent();
    void setComponent(Component component);
}

package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public interface SingleComponentContainer {
    Component getComponent();

    void setComponent(AbstractComponent component);
}

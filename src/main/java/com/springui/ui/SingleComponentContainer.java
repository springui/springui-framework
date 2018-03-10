package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public interface SingleComponentContainer extends ComponentsContainer {

    Component getContent();
    void setContent(Component content);
}

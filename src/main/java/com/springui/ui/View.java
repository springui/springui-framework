package com.springui.ui;

import com.springui.web.UIRequest;

/**
 * @author Stephan Grundner
 */
public interface View {

    default Component getComponent() {
        if (this instanceof Component) {
            return (Component) this;
        }

        throw new IllegalStateException("View is not a component");
    }

    void handleRequest(UIRequest request);
}

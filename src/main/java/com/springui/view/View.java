package com.springui.view;

import com.springui.ui.AbstractComponent;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Stephan Grundner
 */
public interface View {

    default AbstractComponent getComponent() {
        if (this instanceof AbstractComponent) {
            return (AbstractComponent) this;
        }

        throw new IllegalStateException("View is no component");
    }

    void request(WebRequest request);
}

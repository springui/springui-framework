package com.springui.web;

import com.springui.ui.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Stephan Grundner
 */
public interface View {

    default Component getComponent() {
        if (this instanceof Component) {
            return (Component) this;
        }

        throw new IllegalStateException("View is no component");
    }

    MultiValueMap<String, String> getQueryParams();

    void request(WebRequest request);
}

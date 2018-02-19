package com.springui.event;

import com.springui.ui.UI;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Stephan Grundner
 */
public class Action {

    private final WebRequest request;
    private final String componentId;
    private final String event;

    public String getComponentId() {
        return componentId;
    }

    public String getEvent() {
        return event;
    }

    /**
     * Use {@link com.springui.ui.UI#redirectTo(String)} instead.
     */
    @Deprecated
    public void redirectTo(String url) {
        UI.forRequest(request).redirectTo(url);
    }

    public Action(WebRequest request, String componentId, String event) {
        this.request = request;
        this.componentId = componentId;
        this.event = event;
    }
}

package com.springui.event;

import org.springframework.web.context.request.WebRequest;

/**
 * @author Stephan Grundner
 */
public class Action {

    public static final String REDIRECT_URL = Action.class.getName() + "#redirectUrl";

    private final WebRequest request;
    private final String componentId;
    private final String event;

    public String getComponentId() {
        return componentId;
    }

    public String getEvent() {
        return event;
    }

    public void redirectTo(String redirectUrl) {
        request.setAttribute(REDIRECT_URL, redirectUrl, WebRequest.SCOPE_REQUEST);
    }

    public Action(WebRequest request, String componentId, String event) {
        this.request = request;
        this.componentId = componentId;
        this.event = event;
    }
}

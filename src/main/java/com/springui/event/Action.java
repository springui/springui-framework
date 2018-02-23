package com.springui.event;

/**
 * @author Stephan Grundner
 */
public class Action {

    private final String componentId;
    private final String event;

    public String getComponentId() {
        return componentId;
    }

    public String getEvent() {
        return event;
    }

    public Action(String componentId, String event) {
        this.componentId = componentId;
        this.event = event;
    }
}

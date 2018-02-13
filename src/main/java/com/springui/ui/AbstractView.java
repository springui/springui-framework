package com.springui.ui;

import org.springframework.web.context.request.WebRequest;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractView implements View {

    private Component component;
    private boolean initialized;

    @Override
    public final Component getComponent() {
        return component;
    }

    protected final void setComponent(Component component) {
        this.component = component;
    }

    protected abstract void init();

    public void activated(WebRequest request) {
        if (!initialized) {
            initialized = true;
            init();
        }
    }
}

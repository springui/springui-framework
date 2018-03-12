package com.springui.ui;

import com.springui.web.UIRequest;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractView implements View {

    private boolean initialized = false;
    private Component component;

    protected abstract void init(UIRequest request);

    @Override
    public Component getComponent() {
        return component;
    }

    protected void setComponent(Component component) {
        this.component = component;
    }

    @Override
    public void handleRequest(UIRequest request) {
        if (!initialized) {
            init(request);
            initialized = true;
        }

        UI ui = request.getUi();
        Component content = ui.getContent();
        if (content != getComponent()) {
            ui.setContent(getComponent());
        }
    }
}

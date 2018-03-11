package com.springui.ui.view;

import com.springui.ui.Component;
import com.springui.ui.SingleComponentContainer;
import com.springui.ui.UI;
import com.springui.ui.View;
import com.springui.web.UIRequest;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractDecoratorView implements View {

    private boolean initialized = false;
    private UI ui;
    private Component component;

    protected abstract void init(UIRequest request);

    protected UI getUi() {
        return ui;
    }

    @Override
    public Component getComponent() {
        return component;
    }

    protected void setComponent(Component component) {
        this.component = component;
    }

    protected abstract SingleComponentContainer getContainer();

    @Override
    public void handleRequest(UIRequest request) {
        ui = request.getUi();
        if (!initialized) {
            init(request);
            initialized = true;
        }

        UI ui = request.getUi();
        SingleComponentContainer container = getContainer();
        if (ui.getContent() != container) {
            ui.setContent(container);
        }

        Component component = getComponent();
        if (container.getContent() != component) {
            container.setContent(component);
        }
    }
}

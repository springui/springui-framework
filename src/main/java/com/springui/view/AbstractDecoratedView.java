package com.springui.view;

import com.springui.ui.*;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractDecoratedView implements View {

    private Component component;
    private UI ui;

    protected UI getUi() {
        return ui;
    }

    protected abstract SingleComponentContainer getContainer();

    private boolean initialized = false;

    @Override
    public final Component getComponent() {
        return component;
    }

    protected final void setComponent(Component component) {
        this.component = component;
    }

    protected abstract void init(WebRequest request);

    public void request(WebRequest request) {
        if (!initialized) {
            initialized = true;
            init(request);
        }
        UI ui = UI.forRequest(request);
        this.ui = ui;

        SingleComponentContainer container = getContainer();
        container.setComponent(getComponent());
        ui.setRootComponent(container);
    }
}

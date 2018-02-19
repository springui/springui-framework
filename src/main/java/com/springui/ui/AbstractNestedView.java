package com.springui.ui;

import org.springframework.web.context.request.WebRequest;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractNestedView implements View {

    private SingleComponentContainer<Component> container;

    private boolean initialized = false;

    @Override
    public final Component getComponent() {
        return getContainer().getComponent();
    }

    protected final void setComponent(Component component) {
        getContainer().setComponent(component);
    }

    protected abstract SingleComponentContainer<Component> createContainer();

    protected final SingleComponentContainer<Component> getContainer() {
        if (container == null) {
            container = createContainer();
        }

        return container;
    }

    protected abstract void init(WebRequest request);

    public void process(WebRequest request) {
        if (!initialized) {
            initialized = true;
            init(request);
        }
        UI ui = UI.forRequest(request);
        ui.setRootComponent(container);
    }
}

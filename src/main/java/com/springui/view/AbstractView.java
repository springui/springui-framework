package com.springui.view;

import com.springui.ui.AbstractComponent;
import com.springui.ui.UI;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractView implements View {

    private AbstractComponent component;

    private boolean initialized = false;

    @Override
    public final AbstractComponent getComponent() {
        return component;
    }

    protected final void setComponent(AbstractComponent component) {
        this.component = component;
    }

    protected abstract void init(WebRequest request);

    public void request(WebRequest request) {
        if (!initialized) {
            initialized = true;
            init(request);
        }
        UI ui = UI.forRequest(request);
        ui.setRootComponent(getComponent());
    }
}

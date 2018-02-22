package com.springui.view;

import com.springui.ui.Component;
import com.springui.ui.UI;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractView implements View {

    private Component component;
    private MultiValueMap<String, String> queryParams;

    private boolean initialized = false;

    @Override
    public final Component getComponent() {
        return component;
    }

    protected final void setComponent(Component component) {
        this.component = component;
    }

    @Override
    public MultiValueMap<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(MultiValueMap<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    protected abstract void init(WebRequest request);

    public void request(WebRequest request) {
        if (!initialized) {
            initialized = true;
            init(request);
        }
        UI ui = UI.forRequest(request);
        ui.setRootComponent(component);
    }
}

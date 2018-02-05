package com.springui.ui.component;

import org.springframework.util.MultiValueMap;

/**
 * @author Stephan Grundner
 */
public abstract class View extends SingleComponentContainer<Component> {

    {
        setTemplate("ui/view");
    }

    private boolean initialized;
    private MultiValueMap<String, String> params;

    private String path;

    protected abstract void init();

    @Override
    protected void attached() {
        super.attached();
        if (!initialized) {
            initialized = true;
            init();
        }
    }

    public MultiValueMap<String, String> getParams() {
        return params;
    }

    public void setParams(MultiValueMap<String, String> params) {
        this.params = params;
    }

    public String getPath() {
        return path;
    }

    protected void setPath(String path) {
        this.path = path;
    }

    protected void activate() {
        UI ui = getUi();
        ui.setComponent(this);
    }
}

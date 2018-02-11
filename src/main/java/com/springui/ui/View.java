package com.springui.ui;

import org.springframework.util.MultiValueMap;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/view")
public abstract class View extends SingleComponentContainer<Component> {

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

    protected void activate(MultiValueMap<String, String> params) { }

    protected void activate(UI ui, MultiValueMap<String, String> params) {
        ui.setComponent(this);
        activate(params);
    }
}

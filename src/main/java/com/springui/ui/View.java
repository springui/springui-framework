package com.springui.ui;

import com.springui.util.WebRequestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

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
        if (params == null) {
            params = new LinkedMultiValueMap<>();
        }

        return params;
    }

    protected void setParams(MultiValueMap<String, String> params) {
        this.params = params;
    }

    public String getPath() {
        return path;
    }

    protected void setPath(String path) {
        this.path = path;
    }

    protected void activated(WebRequest request) {
        params = WebRequestUtils.getQueryParams(request);
    }
}

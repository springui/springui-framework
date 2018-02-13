package com.springui.web;

import com.springui.ui.UI;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * @author Stephan Grundner
 */
public class UIRequestInterceptor implements WebRequestInterceptor {

    @Override
    public void preHandle(WebRequest request) throws Exception { }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception { }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        UI ui = UI.forRequest(request);
        if (ui != null) {
            ui.activate(null);
        }
    }
}

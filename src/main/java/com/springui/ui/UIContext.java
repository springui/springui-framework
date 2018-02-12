package com.springui.ui;

import com.springui.collection.MapUtils;
import com.springui.web.PathMappings;
import com.springui.web.UIMapping;
import com.springui.util.WebRequestUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Stephan Grundner
 */
public class UIContext implements ApplicationContextAware {

    public static UIContext forRequest(WebRequest request) {
        return (UIContext) request.getAttribute(UIContext.class.getName(), WebRequest.SCOPE_SESSION);
    }

    public static UIContext forRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (UIContext) session.getAttribute(UIContext.class.getName());
        }

        return null;
    }

    private ApplicationContext applicationContext;
    private PathMappings<UIMapping> uiMappings = new PathMappings<>();

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void registerUiClass(String path, Class<? extends UI> uiClass) {
        Assert.hasLength(path, "[path] must not be empty");
        Assert.notNull(uiClass, "[uiClass] must not be null");
        MapUtils.putValueOnce(uiMappings, path, new UIMapping(path, uiClass));
    }

    public UI getUi(String path, boolean create) {
        UIMapping mapping = uiMappings.find(path);
        UI ui = mapping.getUi();
        if (ui == null && create) {
            ui = org.springframework.beans.BeanUtils.instantiate(mapping.getUiClass());
            ui.setPath(mapping.getPath());
            ui.setContext(this);
            ui.init(WebRequestUtils.getCurrentServletRequest());
            mapping.setUi(ui);
        }

        return ui;
    }

    public UI getUi(String path) {
        return getUi(path, true);
    }

    public UI getUi(WebRequest request, boolean create) {
        return getUi(WebRequestUtils.getPath(request), create);
    }

    public UI getUi(WebRequest request) {
        return getUi(request, true);
    }

    public void bindTo(HttpServletRequest request) {
        request.getSession().setAttribute(UIContext.class.getName(), this);
    }
}

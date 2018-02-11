package com.springui.ui;

import com.springui.collection.MapUtils;
import com.springui.util.BeanUtils;
import com.springui.web.PathMappings;
import com.springui.web.UIMapping;
import com.springui.web.WebRequestUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    public static UIContext getCurrent() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return (UIContext) requestAttributes.getAttribute(UIContext.class.getName(), RequestAttributes.SCOPE_SESSION);
    }

    public static void setCurrent(UIContext current) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        requestAttributes.setAttribute(UIContext.class.getName(), current, RequestAttributes.SCOPE_SESSION);
    }

    private ApplicationContext applicationContext;

//    private PathMappings<UI> mappings = new PathMappings<>();
    private PathMappings<UIMapping> mappings = new PathMappings<>();

    private final Map<String, Component> components = new HashMap<>();

    private boolean initialized;

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
        MapUtils.putValueOnce(mappings, path, new UIMapping(path, uiClass));
    }


    public UI findUi(String path) {
        UIMapping mapping = mappings.find(path);
        UI ui = mapping.getUi();
        if (ui == null) {
            ui = org.springframework.beans.BeanUtils.instantiate(mapping.getUiClass());
            ui.setPath(mapping.getPath());
            ui.setContext(this);
            ui.init(WebRequestUtils.getCurrentServletRequest());
            mapping.setUi(ui);
        }

        return ui;
    }

    public UI findUi(WebRequest request) {
        return findUi(WebRequestUtils.getPath(request));
    }

    public Map<String, Component> getComponents() {
        return Collections.unmodifiableMap(components);
    }

    public Component getComponent(String componentId) {
        return components.get(componentId);
    }

    protected boolean attach(Component component) {
        if (components.put(component.getId(), component) == null) {
            component.attached();
            return true;
        }

        return false;
    }

    protected boolean detach(Component component) {
        if (components.remove(component.getId(), component)) {
            component.detached();
            return true;
        }

        return false;
    }

    protected synchronized void init() {
        if (!initialized) {
            initialized = true;


        }
    }

    public void bindTo(HttpServletRequest request) {
        request.getSession().setAttribute(UIContext.class.getName(), this);
    }
}

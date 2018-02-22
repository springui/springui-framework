package com.springui.web;

import com.springui.ui.UI;
import com.springui.util.BeanFactoryUtils;
import com.springui.util.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Stephan Grundner
 */
public class UIContext {

    public static UIContext forSession(HttpSession session) {
        return (UIContext) session.getAttribute(UIContext.class.getName());
    }

    public static UIContext forSession(HttpServletRequest request) {
        return forSession(request.getSession());
    }

    private final ListableBeanFactory beanFactory;
    private final UIMappingRegistry uiMappingRegistry;
    private final Map<String, UI> uiByPath = new HashMap<>();

    public UIMappingRegistry getUiMappingRegistry() {
        return uiMappingRegistry;
    }

    public UI getUi(HttpServletRequest request) {
        UIMappingRegistry.Mapping mapping = uiMappingRegistry.findMapping(request);
        if (mapping != null) {
            AntPathMatcher pathMatcher = new AntPathMatcher();
            UrlPathHelper pathHelper = new UrlPathHelper();
            String path = pathHelper.getPathWithinApplication(request);
            String tail = pathMatcher.extractPathWithinPattern(mapping.getPattern(), path);
            path = StringUtils.removeEnd(path, tail);
            UI ui = uiByPath.get(path);
            if (ui == null) {
                Class<? extends UI> uiClass = mapping.getUiClass();
                ui = BeanFactoryUtils.getPrototypeBean(beanFactory, uiClass);
                MapUtils.putOnce(uiByPath, path, ui);
                ui.init(new ServletWebRequest(request));
            }

            return ui;
        }

        return null;
    }

    public String getPath(UI ui) {
        return uiByPath.entrySet().stream()
                .filter(it -> Objects.equals(it.getValue(), ui))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    protected void bindTo(HttpSession session) {
        session.setAttribute(UIContext.class.getName(), this);
    }

    public UIContext(ListableBeanFactory beanFactory, UIMappingRegistry uiMappingRegistry) {
        this.beanFactory = beanFactory;
        this.uiMappingRegistry = uiMappingRegistry;
    }
}

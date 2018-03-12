package com.springui.beans;

import com.springui.ui.UI;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
public class UIScope implements Scope {

    private static class ScopeData {

        private final Map<String, Object> beans = new HashMap<>();

        public Object getBean(String name) {
            return beans.get(name);
        }

        public void setBean(String name, Object bean) {
            beans.put(name, bean);
        }

        public Object removeBean(String name) {
            return beans.remove(name);
        }
    }

    private HttpServletRequest currentRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getRequest();
    }

    private HttpSession currentSession() {
        HttpServletRequest request = currentRequest();
        return request.getSession(false);
    }

    private ScopeData getOrCreateScopeData(UI ui) {
        ScopeData scopeData = (ScopeData) ui.getAttribute(ScopeData.class.getName());
        if (scopeData == null) {
            scopeData = new ScopeData();
            ui.setAttribute(ScopeData.class.getName(), scopeData);
        }

        return scopeData;
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        UI ui = UI.forSession(currentSession());
        ScopeData scopeData = getOrCreateScopeData(ui);
        Object object = scopeData.getBean(name);
        if (object == null) {
            object = objectFactory.getObject();
            scopeData.setBean(name, object);
        }

        return object;
    }

    @Override
    public Object remove(String name) {
        UI ui = UI.forSession(currentSession());
        ScopeData scopeData = getOrCreateScopeData(ui);
        return scopeData.removeBean(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        UI ui = UI.forSession(currentSession());
        return ui.getId();
    }
}

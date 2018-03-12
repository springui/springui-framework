package com.springui.ui;

import com.springui.util.MapUtils;

import java.util.*;

/**
 * @author Stephan Grundner
 */
public class CustomLayout extends AbstractComponentsContainer implements Layout {

    private Map<String, Component> components = new HashMap<>();
    private String templateName;

    public Set<String> getComponentNames() {
        return Collections.unmodifiableSet(components.keySet());
    }

    public Component getComponent(String name) {
        return components.get(name);
    }

    public Component addComponent(String name, Component component) {
        Component result = removeComponent(name);
        if (component != null) {
            MapUtils.putOnce(components, name, component);
            component.setParent(this);
            ComponentsContainer.notifyAttached(component);
        }

        return result;
    }

    @Override
    public final void addComponent(Component component) {
        addComponent(null, component);
    }

    public Component removeComponent(String name) {
        Component component = components.get(name);
        if (components.remove(name, component)) {
            if (component != null) {
                component.setParent(null);
                ComponentsContainer.notifyDetached(component, this);
            }

            return component;
        }

        return null;
    }

    @Override
    public final void removeComponent(Component component) {
//        TODO Check result and throw exception if necessary!
        removeComponent((String) null);
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        visitor.visit(this);
        for (Component component : components.values()) {
            component.walk(visitor);
        }
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public CustomLayout(String templateName) {
        this.templateName = templateName;
    }

    public CustomLayout() { }
}

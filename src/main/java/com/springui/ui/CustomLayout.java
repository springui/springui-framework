package com.springui.ui;

import com.springui.util.MapUtils;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
public final class CustomLayout extends ComponentsContainer implements Layout {

    private String template;
    private final Map<String, Component> components = new HashMap<>();

    public String getTemplate() {
        return template;
    }

    public Map<String, Component> getComponents() {
        return Collections.unmodifiableMap(components);
    }

    @Override
    public Iterator<Component> iterator() {
        return getComponents().values().iterator();
    }

    public Component getComponent(String name) {
        return components.get(name);
    }

    public Component addComponent(String name, Component component) {
        Component result = removeComponent(name);
        if (component != null) {
            MapUtils.putOnce(components, name, component);
            component.setParent(this);
        }

        return result;
    }

    @Override
    protected void addComponent(Component component) {
        addComponent(null, component);
    }

    public Component removeComponent(String name) {
        Component component = components.get(name);
        if (components.remove(name, component)) {
            if (component != null) {
                component.setParent(null);
            }
            return component;
        }

        return null;
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        super.walk(visitor);
        for (Component component : components.values()) {
            component.walk(visitor);
        }
    }

    public CustomLayout(String template) {
        Assert.hasLength(template, "[template] must not be null");
        this.template = template;
    }
}

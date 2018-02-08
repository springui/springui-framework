package com.springui.ui;

import com.springui.collection.MapUtils;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
public final class Layout extends ComponentsContainer<Component> {

    private final Map<String, Component> components = new HashMap<>();

    public Map<String, Component> getComponents() {
        return Collections.unmodifiableMap(components);
    }

    @Override
    public Iterator<Component> iterator() {
        return getComponents().values().iterator();
    }

    public boolean addComponent(String name, Component component) {
        if (!components.containsKey(name)) {
            MapUtils.putValueOnce(components, name, component);
            component.setParent(this);
            return true;
        }

        return false;
    }

    public boolean removeComponent(String name) {
        Component component = components.get(name);
        if (component != null) {
            if (components.remove(name, component)) {
                component.setParent(null);
                return true;
            }
        }

        return false;
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        super.walk(visitor);
        for (Component component : components.values()) {
            component.walk(visitor);
        }
    }

    public Layout(String template) {
        Assert.hasLength(template, "[template] must not be null");
        setTemplate(template);
    }

    public Layout() { }
}
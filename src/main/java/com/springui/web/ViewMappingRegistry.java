package com.springui.web;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
public class ViewMappingRegistry {

//    private final PathLookup<Class<? extends View>> registrations = new PathLookup<>();
    private final Map<String, Class<? extends View>> registrations = new HashMap<>();

    public void register(String path, Class<? extends View> viewClass) {
        Assert.hasLength(path, "[path] must not be empty");
        Assert.notNull(viewClass, "[viewClass] must not be null");
        registrations.put(path, viewClass);
    }

    public Class<? extends View> findViewClass(String path) {
        return registrations.get(path);
    }

    public String findPath(Class<? extends View> viewClass) {
        return registrations.entrySet().stream()
                .filter(it -> it.getValue().isAssignableFrom(viewClass))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }

    public Map<String, Class<? extends View>> getMappings() {
        return Collections.unmodifiableMap(registrations);
    }
}

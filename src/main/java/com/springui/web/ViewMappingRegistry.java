package com.springui.web;

import com.springui.ui.View;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
public class ViewMappingRegistry {

    private final PathLookup<Class<? extends View>> registrations = new PathLookup<>();

    public void register(String path, Class<? extends View> viewClass) {
        Assert.hasLength(path, "[path] must not be empty");
        Assert.notNull(viewClass, "[viewClass] must not be null");
        registrations.put(path, viewClass);
    }

    public Class<? extends View> lookup(String path) {
        return registrations.lookup(path);
    }

    public Map<String, Class<? extends View>> getMappings() {
        return Collections.unmodifiableMap(registrations);
    }
}

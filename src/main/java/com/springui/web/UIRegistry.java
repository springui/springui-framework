package com.springui.web;

import com.springui.collection.MapUtils;
import com.springui.ui.UI;

/**
 * @author Stephan Grundner
 */
public class UIRegistry {

    private PathMappings<Class<? extends UI>> mapping = new PathMappings<>();

    public void registerUiClass(Class<? extends UI> uiClass, String path) {
        MapUtils.putValueOnce(mapping, path, uiClass);
    }

    public PathMappings<Class<? extends UI>> getMapping() {
        return mapping;
    }
}

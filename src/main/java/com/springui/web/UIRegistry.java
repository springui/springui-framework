package com.springui.web;

import com.springui.collection.MapUtils;
import com.springui.ui.UI;

/**
 * @author Stephan Grundner
 */
public class UIRegistry {

    private UrlMapping<Class<? extends UI>> mapping = new UrlMapping<>();

    public void registerUiClass(Class<? extends UI> uiClass, String path) {
        MapUtils.putValueOnce(mapping, path, uiClass);
    }

    public UrlMapping<Class<? extends UI>> getMapping() {
        return mapping;
    }
}

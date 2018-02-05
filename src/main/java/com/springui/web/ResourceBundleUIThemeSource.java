package com.springui.web;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.ui.context.HierarchicalThemeSource;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.ResourceBundleThemeSource;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Stephan Grundner
 */
public class ResourceBundleUIThemeSource implements HierarchicalThemeSource, UIThemeSource, BeanClassLoaderAware {

    private final ResourceBundleThemeSource themeSource = new ResourceBundleThemeSource();

    private final Map<String, UITheme> themeCache = new WeakHashMap<>();

    private UITheme createTheme(String themeName) {
        return new UITheme(themeSource.getTheme(themeName));
    }

    @Override
    public synchronized UITheme getTheme(String themeName) {
        UITheme theme = themeCache.get(themeName);
        if (theme == null) {
            theme = createTheme(themeName);
            themeCache.put(themeName, theme);
        }

        return theme;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        themeSource.setBeanClassLoader(classLoader);
    }

    @Override
    public void setParentThemeSource(ThemeSource parent) {
        themeSource.setParentThemeSource(parent);
    }

    @Override
    public ThemeSource getParentThemeSource() {
        return themeSource.getParentThemeSource();
    }
}

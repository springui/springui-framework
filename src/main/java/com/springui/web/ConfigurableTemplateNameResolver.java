package com.springui.web;

import com.springui.ui.component.Component;

/**
 * @author Stephan Grundner
 */
public class ConfigurableTemplateNameResolver implements TemplateNameResolver {

    @Override
    public String resolveTemplateName(UITheme theme, Component component) {
        String themeName = theme.getName();
        return null;
    }
}

package com.springui.web;

import com.springui.ui.component.Component;

/**
 * @author Stephan Grundner
 */
public interface TemplateNameResolver {

    String resolveTemplateName(UITheme theme, Component component);
}

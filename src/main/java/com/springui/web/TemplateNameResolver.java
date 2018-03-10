package com.springui.web;

import com.springui.ui.Component;
import org.springframework.ui.context.Theme;

/**
 * @author Stephan Grundner
 */
public interface TemplateNameResolver {

    int getOrder();

    String resolveTemplateName(Theme theme, Component component);
}

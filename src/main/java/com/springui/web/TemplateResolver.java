package com.springui.web;

import com.springui.ui.AbstractComponent;
import com.springui.ui.UI;

/**
 * @author Stephan Grundner
 */
public interface TemplateResolver {

    int getPriority();
    boolean accept(String theme);

    String resolveTemplate(String theme, UI ui);
    String resolveTemplate(String theme, AbstractComponent component);
}

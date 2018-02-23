package com.springui.web;

import com.springui.ui.Component;
import com.springui.ui.CustomLayout;
import com.springui.ui.UI;
import com.springui.util.SlashUtils;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;

/**
 * @author Stephan Grundner
 */
public class CustomLayoutTemplateResolver implements TemplateResolver {

    private PropertyPlaceholderHelper placeholderHelper =
            new PropertyPlaceholderHelper("{", "}");

    @Override
    public int getPriority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean accept(String theme) {
        return true;
    }

    @Override
    public String resolveTemplate(String theme, UI ui) {
        return null;
    }

    @Override
    public String resolveTemplate(String theme, Component component) {
        if (component instanceof CustomLayout) {
            Properties properties = new Properties();
            properties.setProperty("theme", theme);
            String template = ((CustomLayout) component).getTemplate();
            template = placeholderHelper.replacePlaceholders(template, properties);
            template = SlashUtils.removeLeadingAndTrailingSlashes(template);
            return template;
        }

        return null;
    }
}

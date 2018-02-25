package com.springui.web;

import com.springui.ui.AbstractComponent;
import com.springui.ui.Template;
import com.springui.ui.UI;
import com.springui.util.SlashUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;

/**
 * @author Stephan Grundner
 */
public class AnnotationConfigTemplateResolver implements TemplateResolver {

    private PropertyPlaceholderHelper placeholderHelper =
            new PropertyPlaceholderHelper("{", "}");

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean accept(String theme) {
        return true;
    }

    private String findTemplate(String theme, Object componentOrUi) {
        Properties properties = new Properties();
        properties.setProperty("theme", theme);

        Template annotation = AnnotationUtils
                .findAnnotation(componentOrUi.getClass(), Template.class);
        String template = (String) AnnotationUtils.getValue(annotation, "name");
        if (annotation != null) {
            template = placeholderHelper.replacePlaceholders(template, properties);
            template = SlashUtils.removeLeadingAndTrailingSlashes(template);
            return template;
        }

        return null;
    }

    @Override
    public String resolveTemplate(String theme, UI ui) {
        return findTemplate(theme, ui);
    }

    @Override
    public String resolveTemplate(String theme, AbstractComponent component) {
        return findTemplate(theme, component);
    }
}

package com.springui.web;

import com.springui.ui.Component;
import com.springui.ui.Template;
import com.springui.ui.CustomLayout;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.context.Theme;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @author Stephan Grundner
 */
public class DefaultTemplateNameResolver implements TemplateNameResolver {

    private PropertyPlaceholderHelper placeholderHelper =
            new PropertyPlaceholderHelper("{", "}");

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public String resolveTemplateName(Theme theme, Component component) {

        Properties properties = new Properties();
        properties.setProperty("theme", theme.getName());

        if (component instanceof CustomLayout) {
            String templateName = ((CustomLayout) component).getTemplateName();
            templateName = placeholderHelper.replacePlaceholders(templateName, properties);
            if (StringUtils.hasLength(templateName)) {
                return templateName;
            }
        }

        Template annotation = AnnotationUtils.findAnnotation(component.getClass(), Template.class);
        if (annotation != null) {
            String templateName = (String) AnnotationUtils.getValue(annotation, "name");
            templateName = placeholderHelper.replacePlaceholders(templateName, properties);
            if (StringUtils.hasLength(templateName)) {
                return templateName;
            }
        }

        return null;
    }
}

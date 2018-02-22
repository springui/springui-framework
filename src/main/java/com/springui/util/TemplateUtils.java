package com.springui.util;

import com.springui.ui.Template;
import com.springui.web.TemplateProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ThemeResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/**
 * @author Stephan Grundner
 */
public class TemplateUtils {

    public static String resolveTemplate(String theme, TemplateProvider templateProvider) {
        PropertyPlaceholderHelper placeholderHelper =
                new PropertyPlaceholderHelper("{", "}");
        Properties properties = new Properties();
        properties.setProperty("theme", theme);

        String template = templateProvider.getTemplate();
        if (StringUtils.isEmpty(template)) {
            Template annotation = AnnotationUtils
                    .findAnnotation(templateProvider.getClass(), Template.class);
            template = (String) AnnotationUtils.getValue(annotation, "name");
        }

        template = placeholderHelper.replacePlaceholders(template, properties);
        template = SlashUtils.removeLeadingAndTrailingSlashes(template);
        return template;
    }

    public static String resolveTemplate(HttpServletRequest request, ThemeResolver themeResolver, TemplateProvider templateProvider) {
        return resolveTemplate(themeResolver.resolveThemeName(request), templateProvider);
    }
}

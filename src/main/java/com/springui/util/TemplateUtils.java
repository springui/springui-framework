package com.springui.util;

import org.springframework.web.servlet.ThemeResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Stephan Grundner
 */
@Deprecated
public class TemplateUtils {

    @Deprecated
    public static String resolveTemplate(String theme, Object templateProvider) {
//        PropertyPlaceholderHelper placeholderHelper =
//                new PropertyPlaceholderHelper("{", "}");
//        Properties properties = new Properties();
//        properties.setProperty("theme", theme);
//
//        String template = templateProvider.getTemplate();
//        if (StringUtils.isEmpty(template)) {
//            Template annotation = AnnotationUtils
//                    .findAnnotation(templateProvider.getClass(), Template.class);
//            template = (String) AnnotationUtils.getValue(annotation, "name");
//        }
//
//        template = placeholderHelper.replacePlaceholders(template, properties);
//        template = SlashUtils.removeLeadingAndTrailingSlashes(template);
//        return template;
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public static String resolveTemplate(HttpServletRequest request, ThemeResolver themeResolver, Object templateProvider) {
        return resolveTemplate(themeResolver.resolveThemeName(request), templateProvider);
    }
}

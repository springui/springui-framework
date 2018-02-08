package com.springui.web;

import com.springui.ui.Component;
import com.springui.util.SlashUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ThemeResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/**
 * @author Stephan Grundner
 */
public class TemplateUtils {

    public static String resolveTemplate(String theme, Component component) {
        PropertyPlaceholderHelper placeholderHelper =
                new PropertyPlaceholderHelper("{", "}");
        Properties properties = new Properties();
        properties.setProperty("theme", theme);
        String template = component.getTemplate();
        template = placeholderHelper.replacePlaceholders(template, properties);
        template = SlashUtils.removeLeadingAndTrailingSlashes(template);
        return template;
    }

    public static String resolveTemplate(WebRequest request, ThemeResolver themeResolver, Component component) {
        HttpServletRequest servletRequest = WebRequestUtils.toServletRequest(request);
        return resolveTemplate(themeResolver.resolveThemeName(servletRequest), component);
    }

    public static String resolveTemplate(ThemeResolver themeResolver, Component component) {
        WebRequest request = WebRequestUtils.getCurrentWebRequest();
        return resolveTemplate(request, themeResolver, component);
    }
}

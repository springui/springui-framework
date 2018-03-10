package com.springui.web;

import com.springui.beans.BeanFactoryUtils;
import com.springui.ui.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

/**
 * @author Stephan Grundner
 */
public final class TemplateUtils {

    public static String resolveTemplateName(Theme theme, Component component, Collection<TemplateNameResolver> templateNameResolvers) {
        return templateNameResolvers.stream()
                .sorted(Comparator.comparingInt(TemplateNameResolver::getOrder))
                .map(it -> it.resolveTemplateName(theme, component))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public static String resolveTemplateName(HttpServletRequest request,  Component component, Collection<TemplateNameResolver> templateNameResolvers) {
        ApplicationContext applicationContext = RequestContextUtils.findWebApplicationContext(request);
        ThemeResolver themeResolver = applicationContext.getBean(ThemeResolver.class);
        String themeName = themeResolver.resolveThemeName(request);
        ThemeSource themeSource = applicationContext.getBean(ThemeSource.class);
        Theme theme = themeSource.getTheme(themeName);
        return resolveTemplateName(theme, component, templateNameResolvers);
    }

    public static String resolveTemplateName(HttpServletRequest request, Component component) {
        ApplicationContext applicationContext = RequestContextUtils.findWebApplicationContext(request);
        Collection<TemplateNameResolver> templateNameResolvers = BeanFactoryUtils.getSingletonBeans(applicationContext, TemplateNameResolver.class);
        return resolveTemplateName(request, component, templateNameResolvers);
    }

    private TemplateUtils() {}
}

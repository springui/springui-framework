package com.springui.web;

import com.springui.ui.component.Component;
import com.springui.util.SlashUtils;
import org.springframework.context.MessageSource;
import org.springframework.ui.context.Theme;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;

/**
 * @author Stephan Grundner
 */
public class UITheme implements Theme {

    private final Theme delegate;

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public MessageSource getMessageSource() {
        return delegate.getMessageSource();
    }

    @Deprecated
    public String resolveTemplatePath(Component component) {
        PropertyPlaceholderHelper placeholderHelper =
                new PropertyPlaceholderHelper("{", "}");
        Properties properties = new Properties();
        properties.setProperty("theme", getName());
        String template = component.getTemplate();
        template = placeholderHelper.replacePlaceholders(template, properties);
        template = SlashUtils.removeLeadingAndTrailingSlashes(template);
        return template;
    }

    public UITheme(Theme delegate) {
        this.delegate = delegate;
    }
}

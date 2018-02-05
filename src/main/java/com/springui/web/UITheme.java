package com.springui.web;

import com.springui.ui.component.Component;
import com.springui.util.SlashUtils;
import org.springframework.context.MessageSource;
import org.springframework.ui.context.Theme;

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

    public String getTemplate(Component component) {
        String template = component.getTemplate();
        template = SlashUtils.removeLeadingAndTrailingSlashes(template);
        return String.format("%s/%s", getName(), template);
    }

    public UITheme(Theme delegate) {
        this.delegate = delegate;
    }
}

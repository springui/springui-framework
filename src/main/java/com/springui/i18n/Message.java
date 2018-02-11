package com.springui.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @author Stephan Grundner
 */
public class Message {

    private MessageSourceProvider sourceProvider;

    private String code;
    private Object[] args;
    private String defaultText;

    public MessageSourceProvider getSourceProvider() {
        return sourceProvider;
    }

    public void setSourceProvider(MessageSourceProvider sourceProvider) {
        this.sourceProvider = sourceProvider;
    }

    private MessageSource getSource() {
        if (sourceProvider != null) {
            return sourceProvider.getMessageSource();
        }

        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    private Locale getLocale() {
        if (sourceProvider != null) {
            return sourceProvider.getLocale();
        }

        return LocaleContextHolder.getLocale();
    }

    public String getText() {
        MessageSource source = getSource();
        if (source != null) {
            Locale locale = getLocale();
            if (locale == null) {
                locale = LocaleContextHolder.getLocale();
            }

            return source.getMessage(code, args, defaultText, locale);
        }

        return defaultText;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public Message(MessageSourceProvider sourceProvider) {
        this.sourceProvider = sourceProvider;
    }

    public Message(MessageSourceProvider sourceProvider, String code, Object[] args, String defaultText) {
        this.sourceProvider = sourceProvider;
        this.code = code;
        this.args = args;
        this.defaultText = defaultText;
    }

    public Message(MessageSourceProvider sourceProvider, String code, String defaultText) {
        this(sourceProvider, code, null, defaultText);
    }

    public Message(MessageSourceProvider sourceProvider, String code) {
        this(sourceProvider, code, null, null);
    }

    public Message(String defaultText) {
        this.defaultText = defaultText;
    }

    public Message() {}
}

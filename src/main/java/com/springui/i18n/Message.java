package com.springui.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @author Stephan Grundner
 */
public class Message {

    public static Message fromText(String text) {
        Message message = new Message();
        message.setDefaultText(text);

        return message;
    }

    private MessageSource source;

    private String code;
    private Object[] args;
    private String defaultText;

    public MessageSource getSource() {
        return source;
    }

    public void setSource(MessageSource source) {
        this.source = source;
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
        return LocaleContextHolder.getLocale();
    }

    public String getText() {
        MessageSource source = getSource();
        if (source != null) {
            Locale locale = getLocale();
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

    public Message(MessageSource source, String code, Object[] args, String defaultText) {
        this.source = source;
        this.code = code;
        this.args = args;
        this.defaultText = defaultText;
    }

    public Message(MessageSource source, String code, String defaultText) {
        this(source, code, null, defaultText);
    }

    public Message(MessageSource source, String code) {
        this(source, code, null, null);
    }

    public Message(String code, Object[] args, String defaultText) {
        this(null, code, args, defaultText);
    }

    public Message(String code, String defaultText) {
        this(null, code, null, defaultText);
    }

    public Message(String code) {
        this((MessageSource) null, code);
    }

    public Message() {}
}

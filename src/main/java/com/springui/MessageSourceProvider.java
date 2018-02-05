package com.springui;

import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author Stephan Grundner
 */
public interface MessageSourceProvider {

    MessageSource getMessageSource();
    Locale getLocale();
}

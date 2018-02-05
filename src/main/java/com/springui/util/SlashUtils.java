package com.springui.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Stephan Grundner
 */
public final class SlashUtils {

    public static final String SLASH = "/";

    public static String removeLeadingSlash(String text) {
        return StringUtils.removeStart(text, SLASH);
    }

    public static String removeTrailingSlash(String text) {
        return StringUtils.removeEnd(text, SLASH);
    }

    public static String removeLeadingAndTrailingSlashes(String text) {
        text = removeLeadingSlash(text);
        return removeTrailingSlash(text);
    }

    public static String prependSlashIfMissing(String text) {
        return StringUtils.prependIfMissing(text, SLASH);
    }

    public static String appendSlashIfMissing(String text) {
        return StringUtils.appendIfMissing(text, SLASH);
    }

    private SlashUtils() {}
}

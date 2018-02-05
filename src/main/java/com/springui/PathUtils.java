package com.springui;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Stephan Grundner
 */
public final class PathUtils {

    public static String normalize(String path) {
        path = StringUtils.prependIfMissing(path, "/");
        return StringUtils.removeEnd(path, "/");
    }

    private PathUtils() {}
}

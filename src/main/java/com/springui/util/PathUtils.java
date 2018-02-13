package com.springui.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Stephan Grundner
 */
public final class PathUtils {

    public static String normalize(String path) {
        path = StringUtils.prependIfMissing(path, "/");
        if (path.length() > 1) {
            return StringUtils.removeEnd(path, "/");
        }

        return path;
    }

    private PathUtils() {}
}

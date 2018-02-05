package com.springui;

import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Stephan Grundner
 */
public final class WebRequestUtils {

    private static String getPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    private static String getPath(ServletWebRequest request) {
        return getPath((HttpServletRequest) request.getNativeRequest());
    }

    public static String getPath(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            return getPath((ServletWebRequest) request);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static String getQueryString(HttpServletRequest request) {
        return request.getQueryString();
    }

    private static String getQueryString(ServletWebRequest request) {
        return getQueryString((HttpServletRequest) request.getNativeRequest());
    }

    public static String getQueryString(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            return getQueryString((ServletWebRequest) request);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private WebRequestUtils() {}
}

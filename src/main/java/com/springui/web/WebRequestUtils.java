package com.springui.web;

import org.springframework.web.context.request.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Stephan Grundner
 */
public final class WebRequestUtils {

    public static WebRequest toWebRequest(HttpServletRequest request) {
        return new ServletWebRequest(request);
    }

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

    public static HttpServletRequest getServletRequest(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            return (HttpServletRequest) ((ServletWebRequest) request).getNativeRequest();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static WebRequest getCurrentWebRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

        if (requestAttributes instanceof WebRequest) {
            return (WebRequest) requestAttributes;
        }

        if (requestAttributes instanceof ServletRequestAttributes) {
            return toWebRequest(((ServletRequestAttributes) requestAttributes).getRequest());
        }

        return null;
    }

    public static HttpServletRequest getCurrentServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        return null;
    }

    private WebRequestUtils() {}
}

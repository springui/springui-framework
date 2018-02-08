package com.springui.web;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Iterator;

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

    public static HttpServletRequest toServletRequest(WebRequest request) {
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

    public static MultiValueMap<String, String> getQueryParams(WebRequest request) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        Iterator<String> paramNameItr = request.getParameterNames();
        while (paramNameItr.hasNext()) {
            String paramName = paramNameItr.next();
            String[] paramValues = request.getParameterValues(paramName);
            queryParams.put(paramName, Arrays.asList(paramValues));
        }

        return queryParams;
    }

    private WebRequestUtils() {}
}

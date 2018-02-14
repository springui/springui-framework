package com.springui.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Stephan Grundner
 */
public final class WebRequestUtils {

    public static HttpServletRequest toServletRequest(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            return (HttpServletRequest) ((ServletWebRequest) request).getNativeRequest();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static String getQueryString(ServletWebRequest request) {
        return HttpServletRequestUtils.getQueryString((HttpServletRequest) request.getNativeRequest());
    }

    public static String getQueryString(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            return getQueryString((ServletWebRequest) request);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @see {@link HttpServletRequestUtils#getUrl(HttpServletRequest)}
     * @param request A servlet request
     * @return A full URL including the query string.
     */
    public static String getUrl(ServletWebRequest request) {
        return HttpServletRequestUtils.getUrl((HttpServletRequest) request.getNativeRequest());
    }

    /**
     * Get the full URL, including the query string,
     * for the specified request.
     *
     * @see {@link #getUrl(ServletWebRequest)}
     * @param request A web request
     * @return A full URL including the query string.
     */
    public static String getUrl(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            return getUrl((ServletWebRequest) request);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static WebRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

        if (requestAttributes instanceof WebRequest) {
            return (WebRequest) requestAttributes;
        }

        if (requestAttributes instanceof ServletRequestAttributes) {
            return HttpServletRequestUtils.toWebRequest(((ServletRequestAttributes) requestAttributes).getRequest());
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

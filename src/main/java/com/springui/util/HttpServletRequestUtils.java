package com.springui.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.*;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author Stephan Grundner
 */
public class HttpServletRequestUtils {

    public static WebRequest toWebRequest(HttpServletRequest request) {
        return new ServletWebRequest(request);
    }

    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static String getQueryString(HttpServletRequest request) {
        return request != null ? request.getQueryString() : null;
    }

    public static MultiValueMap<String, String> getQueryParams(HttpServletRequest request) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        Enumeration<String> paramNameItr = request.getParameterNames();
        while (paramNameItr.hasMoreElements()) {
            String paramName = paramNameItr.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            queryParams.put(paramName, Arrays.asList(paramValues));
        }

        return queryParams;
    }

    public static String getUrl(HttpServletRequest request) {
        UrlPathHelper pathHelper = new UrlPathHelper();
        String url = pathHelper.getPathWithinApplication(request);
        String queryString = getQueryString(request);
        if (StringUtils.hasLength(queryString)) {
            url = String.format("%s?%s", url, queryString);
        }

        return url;
    }

    public static String getPath(HttpServletRequest request) {
        UrlPathHelper pathHelper = new UrlPathHelper();
        return pathHelper.getPathWithinApplication(request);
    }
}

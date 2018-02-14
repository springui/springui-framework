package com.springui.util;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.*;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Stephan Grundner
 */
public class HttpServletRequestUtils {

    public static WebRequest toWebRequest(HttpServletRequest request) {
        return new ServletWebRequest(request);
    }

    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        return null;
    }

    public static String getQueryString(HttpServletRequest request) {
        return request != null ? request.getQueryString() : null;
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
}

package com.springui.thymeleaf.expression;

import com.springui.ui.Component;
import com.springui.util.MapUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Stephan Grundner
 */
public class Urls {

    private UrlPathHelper pathHelper = new UrlPathHelper();

    private static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public String action(HttpServletRequest request, Component component, String... events) {
        String path = pathHelper.getPathWithinApplication(request);

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(path)
                .queryParam("component", component.getId());


        if (events != null && events.length > 0) {
            builder.queryParams(MapUtils.toMap("event", events));
        }

        String queryString = request.getQueryString();
        if (queryString != null) {
            builder.queryParam("return-to", path + queryString);
        }

        return builder.toUriString();
    }

    public String action(Component component, String... events) {
        HttpServletRequest request = getCurrentRequest();
        return action(request, component, events);
    }

//    public String upload(HttpServletRequest request, Component component, String token) {
//        Assert.hasText(token, "[token] must not be empty");
//        String path = pathHelper.getPathWithinApplication(request);
//        return UriComponentsBuilder.fromPath(path)
//                .pathSegment("upload")
//                .queryParam("component", component.getId())
//                .queryParam("token", token)
//                .toUriString();
//    }
//
//    public String upload(Component component, String token) {
//        HttpServletRequest request = HttpServletRequestUtils.getCurrentRequest();
//        return upload(request, component, token);
//    }
}

package com.springui.thymeleaf.expression;

import com.springui.ui.Component;
import com.springui.util.HttpServletRequestUtils;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Stephan Grundner
 */
public class Urls {

    private UrlPathHelper pathHelper = new UrlPathHelper();

    public String action(HttpServletRequest request, Component component) {
        String path = pathHelper.getPathWithinApplication(request);
        String returnUrl = HttpServletRequestUtils.getUrl(request);
        return UriComponentsBuilder.fromPath(path)
//                .pathSegment("action")
                .queryParam("component", component.getId())
                .queryParam("return-to", returnUrl)
                .toUriString();
    }

    public String action(Component component) {
        HttpServletRequest request = HttpServletRequestUtils.getCurrentRequest();
        return action(request, component);
    }

    public String upload(HttpServletRequest request, Component component, String token) {
        Assert.hasText(token, "[token] must not be empty");
        String path = pathHelper.getPathWithinApplication(request);
        return UriComponentsBuilder.fromPath(path)
                .pathSegment("upload")
                .queryParam("component", component.getId())
                .queryParam("token", token)
                .toUriString();
    }

    public String upload(Component component, String token) {
        HttpServletRequest request = HttpServletRequestUtils.getCurrentRequest();
        return upload(request, component, token);
    }
}

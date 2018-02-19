package com.springui.web;

import com.springui.event.Action;
import com.springui.ui.Component;
import com.springui.ui.UI;
import com.springui.util.WebRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stephan Grundner
 */
public class UIActionController extends AbstractUIController {

    @Autowired
    private ViewMappingRegistry viewMappingRegistry;

    public String handleRequest(WebRequest request) throws Exception {
        UI ui = UI.forRequest(request);

        bindUi(ui, request);

        MultiValueMap<String, String> params = WebRequestUtils.getQueryParams(request);
        String componentId = params.getFirst("component");
        String event = params.getFirst("event");
        Component component = ui.getComponent(componentId);
        component.performAction(new Action(request, componentId, event));

        String redirectUrl = ui.getRedirectUrl(request);
        if (!StringUtils.isEmpty(redirectUrl)) {
            return redirectUrl;
        } else {
            String path = WebRequestUtils.getPath(request);
            path = path.substring(0, path.length() - "/action".length());
            View view = ui.getView(path);
            Assert.notNull(view);
            MultiValueMap<String, String> queryParams = view.getQueryParams();
            if (queryParams != null) {
                redirectUrl = UriComponentsBuilder.fromPath(path)
                        .queryParams(queryParams)
                        .toUriString();
                return redirectUrl;
            } else {
                return path;
            }
        }
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String redirectUrl = handleRequest(new ServletWebRequest(request, response));
        return new ModelAndView(String.format("redirect:%s", redirectUrl));
    }
}

package com.springui.web;

import com.springui.event.Action;
import com.springui.ui.Component;
import com.springui.ui.UI;
import com.springui.util.WebRequestUtils;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebRequestDataBinder;
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

    public ModelAndView handleRequest(WebRequest request) throws Exception {
        UI ui = UI.forRequest(request);

        bindUi(ui, request);

        MultiValueMap<String, String> params = WebRequestUtils.getQueryParams(request);
        String componentId = params.getFirst("component");
        String event = params.getFirst("event");
        Component component = ui.getComponent(componentId);
        component.performAction(new Action(request, componentId, event));

        ModelAndView modelAndView = new ModelAndView();

        String redirectUrl = ui.getRedirectUrl(request);
        if (!StringUtils.isEmpty(redirectUrl)) {
            modelAndView.setViewName("redirect:" + redirectUrl);
        } else {
            View view = ui.getView(request);
            Assert.notNull(view);
            String path = ui.getPath(view.getClass());
            MultiValueMap<String, String> queryParams = view.getQueryParams();
            if (queryParams != null) {
                String viewName = UriComponentsBuilder.fromPath("redirect:" + path)
                        .queryParams(queryParams)
                        .toUriString();
                modelAndView.setViewName(viewName);
                return modelAndView;
            } else {
                modelAndView.setViewName("redirect:" + path);
            }
        }

        return modelAndView;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return handleRequest(new ServletWebRequest(request, response));
    }
}

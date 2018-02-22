package com.springui.web;

import com.springui.event.Action;
import com.springui.ui.Component;
import com.springui.ui.UI;
import com.springui.util.BeanFactoryUtils;
import com.springui.util.HttpServletRequestUtils;
import com.springui.util.TemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.CacheControl;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ThemeResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stephan Grundner
 */
public class UIRequestHandler extends AbstractUIHandler {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UIMappingRegistry uiMappingRegistry;

    @Autowired
    private ThemeResolver themeResolver;

    @ModelAttribute(UI.ATTRIBUTE_NAME)
    protected UI createUi(HttpServletRequest request) {
        UI ui = UI.forRequest(request);
        if (ui == null) {
            ui = BeanFactoryUtils.getPrototypeBean(applicationContext, UI.class);
            ui.init(new ServletWebRequest(request));
        }

        return ui;
    }

    private void handlePostRequest(
            UI ui,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        bindUi(ui, request);

        MultiValueMap<String, String> params = HttpServletRequestUtils.getQueryParams(request);
        String componentId = params.getFirst("component");
        String event = params.getFirst("event");
        String returnUrl = params.getFirst("return-to");
        Component component = ui.getComponent(componentId);
        component.performAction(new Action(webRequest, componentId, event));

        String redirectUrl = ui.getRedirectUrl(webRequest);
        if (!StringUtils.isEmpty(redirectUrl)) {
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect(returnUrl);
        }
    }

    private ModelAndView handleGetRequest(
            UI ui,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ui.process(request);

        WebDataBinder dataBinder = bindUi(ui, request);

        String redirectUrl = ui.getRedirectUrl(request);
        if (!StringUtils.isEmpty(redirectUrl)) {
            response.sendRedirect(redirectUrl);
            return null;
        }

        ModelAndView modelAndView = new ModelAndView();

        // Required by view layer!
        BindingResult bindingResult = dataBinder.getBindingResult();
        modelAndView.addObject("org.springframework.validation.BindingResult.ui", bindingResult);

        String template = TemplateUtils.resolveTemplate(request, themeResolver, ui);
        modelAndView.setViewName(template);
        modelAndView.addObject("self", ui);

        return modelAndView;
    }

    @Override
    protected ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        UIContext uiContext = UIContext.forSession(request);
        if (uiContext == null) {
            uiContext = new UIContext(applicationContext, uiMappingRegistry);
            uiContext.bindTo(request.getSession(true));
        }

        setCacheControl(CacheControl.noStore());

        UI ui = UI.forRequest(request);
        if (ui == null) {
            ui = createUi(request);
        }

        String requestMethod = request.getMethod();
        if ("POST".equalsIgnoreCase(requestMethod)) {
            handlePostRequest(ui, request, response);
        } else if ("GET".equalsIgnoreCase(requestMethod)) {
            return handleGetRequest(ui, request, response);
        }

        return null;
    }
}

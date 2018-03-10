package com.springui.web;

import com.springui.ui.Component;
import com.springui.ui.UI;
import com.springui.ui.View;
import com.springui.web.servlet.ServletUIRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stephan Grundner
 */
public class ViewController extends AbstractController {

    private final Class<? extends View> viewClass;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UI ui = UI.forSession(request.getSession(true));

        ServletRequestDataBinder requestDataBinder =
                new ServletRequestDataBinder(ui, "ui");
        requestDataBinder.bind(request);

        String method = request.getMethod();
        if ("POST".equalsIgnoreCase(method)) {
            String componentId = request.getParameter("component");
            if (StringUtils.hasLength(componentId)) {
                Component component = ui.getComponents().get(componentId);

                String[] events = request.getParameterValues("event");
                Component.Action action = new Component.Action(
                        new ServletUIRequest(request, response),
                        component, events);
                Component.performAction(action);

                String redirectUrl = action.getRequest().getRedirectUrl();
                if (StringUtils.hasLength(redirectUrl)) {
                    response.sendRedirect(redirectUrl);
                    return null;
                }

                redirectUrl = request.getParameter("return-to");
                if (StringUtils.hasLength(redirectUrl)) {
                    response.sendRedirect(redirectUrl);
                    return null;
                }

                UrlPathHelper pathHelper = new UrlPathHelper();
                String path = pathHelper.getPathWithinApplication(request);
                response.sendRedirect(path);
                return null;
            }
        }

        ApplicationContext applicationContext = RequestContextUtils
                .findWebApplicationContext(request);
        View view = applicationContext.getBean(viewClass);
        view.handleRequest(new ServletUIRequest(request, response));

        ModelAndView modelAndView = new ModelAndView();
        // Required by view layer!
        BindingResult bindingResult = requestDataBinder.getBindingResult();
        modelAndView.addObject("org.springframework.validation.BindingResult.ui", bindingResult);
        modelAndView.addObject("ui", ui);

        String templateName = TemplateUtils.resolveTemplateName(request, ui);
        modelAndView.setViewName(templateName);

        return modelAndView;
    }

    public ViewController(Class<? extends View> viewClass) {
        this.viewClass = viewClass;
    }
}

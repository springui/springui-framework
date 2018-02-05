package com.springui.ui;

import com.springui.event.Action;
import com.springui.ui.component.Component;
import com.springui.ui.component.UI;
import com.springui.ui.component.View;
import com.springui.web.UITheme;
import com.springui.web.UIThemeSource;
import com.springui.web.WebRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Stephan Grundner
 */
@SessionAttributes("ui")
public abstract class UIController {

    public abstract UI createUi();

    @Autowired
    private UIThemeSource themeSource;

    @Autowired
    private ApplicationContext applicationContext;


    @ModelAttribute("ui")
    protected UI init(HttpServletRequest request) {
        UI ui = createUi();

        request.setAttribute(UI.class.getName(), ui);

        UriComponents uriComponents = MvcUriComponentsBuilder
                .fromController(this.getClass()).build();
        ui.setPath(uriComponents.getPath());

        ui.init(request);

        return ui;
    }

    @GetMapping(path = "/**")
    protected String respond(@ModelAttribute("ui") UI ui,
                             BindingResult bindingResult,
                             WebRequest request,
                             Model model) {

        request.setAttribute(UI.class.getName(), ui, WebRequest.SCOPE_REQUEST);

        View view = ui.navigate(request);

        UriComponents uriComponents = UriComponentsBuilder
                .fromPath(WebRequestUtils.getPath(request))
                .query(WebRequestUtils.getQueryString(request))
                .build();

        view.setParams(uriComponents.getQueryParams());

        model.addAttribute("self", ui);

        UITheme theme = ui.getTheme();
        return theme.getTemplate(ui);
    }

    @PostMapping(path = "action")
    protected String request(@ModelAttribute("ui") UI ui,
                             BindingResult bindingResult,
                             @RequestParam(name = "component") String componentId,
                             @RequestParam(name = "event") String event,
                             WebRequest request) {

        request.setAttribute(UI.class.getName(), ui, WebRequest.SCOPE_REQUEST);

        Component component = ui.getComponent(componentId);
        component.performAction(new Action(request, componentId, event));

        String redirectUrl;

        redirectUrl = (String) request.getAttribute(Action.REDIRECT_URL, WebRequest.SCOPE_REQUEST);
        if (!StringUtils.isEmpty(redirectUrl)) {
            return "redirect:" + redirectUrl;
        }

        View view = ui.getActiveView();

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromPath(ui.getPath() + "/" + view.getPath());

        builder.queryParams(view.getParams());
        String uri = builder.build().toUriString();

        return "redirect:" + uri;
    }
}

package com.springui.web;

import com.springui.event.Action;
import com.springui.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Stephan Grundner
 */
@SessionAttributes("uiContext")
public class UIController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThemeResolver themeResolver;

    protected void initContext(UIContext context) {}

    @ModelAttribute("uiContext")
    protected UIContext uiForCurrentSession(HttpServletRequest request) {
        UIContext context = UIContext.forRequest(request);
        if (context == null) {
            context = new UIContext();
            context.setApplicationContext(applicationContext);
            context.bindTo(request);
            initContext(context);
        }

        return context;
    }

    @ModelAttribute("ui")
    protected UI uiForCurrentRequest(@ModelAttribute("uiContext") UIContext context,
                                     BindingResult bindingResult,
                                     WebRequest request) {

        UI ui = context.findUi(request);
        ui.setCurrent();

        return ui;
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        UIContext uiContext = UIContext.getCurrent();
        uiContext.getComponents().forEach((id, component) -> {
            if (component instanceof Field) {

                Field field = (Field) component;
                String propertyPath = String.format("components[%s].value", field.getId());
                if (field instanceof DateField) {
                    // ISO 8601
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    webDataBinder.registerCustomEditor(Date.class, propertyPath,
                            new CustomDateEditor(dateFormat, true));
                }

            }
        });
    }

    @GetMapping(path = "/**")
    protected String respond(@ModelAttribute("uiContext") UIContext uiContext,
                             BindingResult bindingResult,
                             WebRequest request,
                             Model model) {

        UI ui = uiContext.findUi(request);

        View view = ui.navigate(request);

        UriComponents uriComponents = UriComponentsBuilder
                .fromPath(WebRequestUtils.getPath(request))
                .query(WebRequestUtils.getQueryString(request))
                .build();

        view.setParams(uriComponents.getQueryParams());

        model.addAttribute("uiContext", uiContext);
        model.addAttribute("self", ui);

        return TemplateUtils.resolveTemplate(request, themeResolver, ui);
    }

    private String redirect(UI ui) {
        View view = ui.getActiveView();

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromPath(ui.getPath() + "/" + view.getPath());

        builder.queryParams(view.getParams());
        String uri = builder.build().toUriString();

        return "redirect:" + uri;
    }

    @PostMapping(path = "action")
    protected String action(@ModelAttribute("uiContext") UIContext uiContext,
                            BindingResult bindingResult,
                            @RequestParam(name = "component") String componentId,
                            @RequestParam(name = "event") String event,
                            WebRequest request) {

        UI ui = uiContext.findUi(request);

        Component component = uiContext.getComponent(componentId);
        component.performAction(new Action(request, componentId, event));

        String redirectUrl;

        redirectUrl = (String) request.getAttribute(Action.REDIRECT_URL, WebRequest.SCOPE_REQUEST);
        if (!StringUtils.isEmpty(redirectUrl)) {
            return "redirect:" + redirectUrl;
        }

        return redirect(ui);
    }

    @PostMapping(path = "upload")
    protected String upload(@ModelAttribute("uiContext") UIContext uiContext,
                            BindingResult bindingResult,
                            @RequestParam(name = "component") String componentId,
                            @RequestParam(name = "token") String token,
                            WebRequest request,
                            MultipartRequest files) {

        UI ui = uiContext.findUi(request);

        Upload upload = (Upload) uiContext.getComponent(componentId);

        MultipartFile file = files.getFile(token);
        if (!file.isEmpty()) {
            Upload.UploadHandler handler = upload.getHandler();
            if (handler != null) {
                handler.receive(request, file);
            }
        }

        return redirect(ui);
    }
}

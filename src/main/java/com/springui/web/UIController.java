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

    protected void initContext(UIContext context) { }

    @ModelAttribute("uiContext")
    protected final UIContext createContext(HttpServletRequest request) {
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
    protected UI ui(@ModelAttribute("uiContext") UIContext context,
                    BindingResult bindingResult,
                    WebRequest request) {

        UI ui = context.getUi(request);
        ui.bindTo(request);

        return ui;
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder, WebRequest request) {
        UIContext uiContext = UIContext.forRequest(request);
        UI ui = uiContext.getUi(request);
        ui.getComponents().forEach((id, component) -> {
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
    protected String respond(@ModelAttribute("ui") UI ui,
                             BindingResult bindingResult,
                             WebRequest request,
                             Model model) {

        View view = ui.activate(request);
        view.setParams(WebRequestUtils.getQueryParams(request));

        model.addAttribute("self", ui);

        return TemplateUtils.resolveTemplate(request, themeResolver, ui);
    }

    protected String respond(UI ui) {
        View view = ui.getActiveView();

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .pathSegment(ui.getPath(), view.getPath())
                .queryParams(view.getParams())
                .build();

        String uri = uriComponents.toUriString();

        return "redirect:" + uri;
    }

    @PostMapping(path = "action")
    protected String action(@ModelAttribute("ui") UI ui,
                            BindingResult bindingResult,
                            @RequestParam(name = "component") String componentId,
                            @RequestParam(name = "event") String event,
                            WebRequest request) {

//        UI ui = uiContext.getUi(request);
        Component component = ui.getComponent(componentId);
        component.performAction(new Action(request, componentId, event));

        String redirectUrl;

        redirectUrl = (String) request.getAttribute(Action.REDIRECT_URL, WebRequest.SCOPE_REQUEST);
        if (!StringUtils.isEmpty(redirectUrl)) {
            return "redirect:" + redirectUrl;
        }

        return respond(ui);
    }

    @PostMapping(path = "upload")
    protected String upload(@ModelAttribute("ui") UI ui,
                            BindingResult bindingResult,
                            @RequestParam(name = "component") String componentId,
                            @RequestParam(name = "token") String token,
                            WebRequest request,
                            MultipartRequest files) {

//        UI ui = uiContext.getUi(request);
        Upload upload = (Upload) ui.getComponent(componentId);

        MultipartFile file = files.getFile(token);
        if (!file.isEmpty()) {
            Upload.UploadHandler handler = upload.getHandler();
            if (handler != null) {
                handler.receive(request, file);
            }
        }

        return respond(ui);
    }
}

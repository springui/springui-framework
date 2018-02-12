package com.springui.web;

import com.springui.event.Action;
import com.springui.ui.*;
import com.springui.util.TemplateUtils;
import com.springui.util.WebRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.ui.Model;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Stephan Grundner
 */
@SessionAttributes("ui")
public abstract class UIController {

    private static final Logger LOG = LoggerFactory.getLogger(UIController.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThemeResolver themeResolver;

    protected void init(UI ui, WebRequest request) {
        ThemeSource themeSource = applicationContext.getBean(ThemeSource.class);
        ThemeResolver themeResolver = applicationContext.getBean(ThemeResolver.class);
        String themeName = themeResolver.resolveThemeName(WebRequestUtils.toServletRequest(request));
        Theme theme = themeSource.getTheme(themeName);
        ui.setTheme(theme);
        ui.bindTo(request);
        ui.init(request);
    }

    @ModelAttribute("ui")
    protected final UI createUi(WebRequest request) {
        UI ui = UI.forRequest(request);
        if (ui == null) {
            ui = applicationContext.getBean(UI.class);
            init(ui, request);
        }

        return ui;
    }

    private void initBinder(UI ui, WebDataBinder webDataBinder) {
        ui.getComponents().forEach((id, component) -> {
            if (component instanceof Field) {

                Field field = (Field) component;
                String propertyPath = String.format("components[%s].value", field.getId());
                if (field instanceof DateField) {
                    // ISO 8601
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    webDataBinder.registerCustomEditor(Date.class, propertyPath,
                            new CustomDateEditor(dateFormat, true));

                    webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"), propertyPath);
                }

            }
        });
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        Object target = webDataBinder.getTarget();
        if (target instanceof UI) {
            initBinder((UI) target, webDataBinder);
        }
    }

    @RequestMapping(path = "**", method = {RequestMethod.GET, RequestMethod.POST})
    protected String request(@ModelAttribute("ui") UI ui,
                             BindingResult bindingResult,
                             WebRequest request,
                             Model model) {

        ui.activate(request);

        model.addAttribute("self", ui);

        return TemplateUtils.resolveTemplate(request, themeResolver, ui);
    }

    protected String respond(UI ui) {
        View view = ui.getActiveView();

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .pathSegment(view.getPath())
                .queryParams(view.getParams())
                .build();

        String uri = uriComponents.toUriString();

        return "redirect:" + uri;
    }

    @PostMapping(path = "**/action")
    protected String action(@ModelAttribute("ui") UI ui,
                            BindingResult bindingResult,
                            @RequestParam(name = "component") String componentId,
                            @RequestParam(name = "event") String event,
                            WebRequest request) {

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

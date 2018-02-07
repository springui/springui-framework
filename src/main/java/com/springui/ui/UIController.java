package com.springui.ui;

import com.springui.event.Action;
import com.springui.ui.component.*;
import com.springui.web.UITheme;
import com.springui.web.UIThemeSource;
import com.springui.web.WebRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.util.Date;

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

    @InitBinder("ui")
    private void initBinder(WebDataBinder webDataBinder) {
        UI ui = (UI) webDataBinder.getTarget();

        ui.getComponents().forEach( (id, component) -> {
            if (component instanceof Field) {

                Field field = (Field) component;
                String propertyPath = String.format("components[%s].value", field.getId());
                if (field instanceof DateField) {
                    DateField dateField = (DateField) field;
                    webDataBinder.registerCustomEditor(Date.class, propertyPath, new PropertyEditorSupport() {
                        @Override
                        public String getAsText() {
                            Date date = (Date) getValue();
                            if (date != null) {
                                return date.toString();
                            }

                            return null;
                        }

                        @Override
                        public void setAsText(String text) throws IllegalArgumentException {
                            setValue(new Date());
                        }
                    });
                }

            }
        });
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
        return theme.resolveTemplatePath(ui);
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
    protected String action(@ModelAttribute("ui") UI ui,
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

        return redirect(ui);
    }

    @PostMapping(path = "upload")
    protected String upload(@ModelAttribute("ui") UI ui,
                            BindingResult bindingResult,
                            @RequestParam(name = "component") String componentId,
                            @RequestParam(name = "token") String token,
                            WebRequest request,
                            MultipartRequest files) {

        request.setAttribute(UI.class.getName(), ui, WebRequest.SCOPE_REQUEST);

        Upload upload = (Upload) ui.getComponent(componentId);

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

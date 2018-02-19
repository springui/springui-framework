package com.springui.web;

import com.springui.ui.UI;
import com.springui.util.BeanFactoryUtils;
import com.springui.util.TemplateUtils;
import com.springui.util.WebRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.CacheControl;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ThemeResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stephan Grundner
 */
public class UIRequestController extends AbstractUIController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThemeResolver themeResolver;

    @ModelAttribute(UI.ATTRIBUTE_NAME)
    protected UI createUi(WebRequest request) {
        UI ui = UI.forRequest(request);
        if (ui == null) {
            ui = BeanFactoryUtils.getPrototypeBean(applicationContext, UI.class);
            ui.init(request);
        }

        return ui;
    }

    public ModelAndView handleRequest(WebRequest request) throws Exception {
        UI ui = UI.forRequest(request);
        if (ui == null) {
            ui = createUi(request);
        }

        ui.process(request);

        WebDataBinder dataBinder = bindUi(ui, request);
        BindingResult bindingResult = dataBinder.getBindingResult();

        ModelAndView modelAndView = new ModelAndView();

        // Required!
        modelAndView.addObject("org.springframework.validation.BindingResult.ui", bindingResult);

        String viewName = TemplateUtils.resolveTemplate(request, themeResolver, ui);
        modelAndView.setViewName(viewName);

        modelAndView.addObject("self", ui);

        return modelAndView;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CacheControl noStore = CacheControl.noStore();
        response.addHeader("Cache-Control", noStore.getHeaderValue());

        return handleRequest(new ServletWebRequest(request, response));
    }
}

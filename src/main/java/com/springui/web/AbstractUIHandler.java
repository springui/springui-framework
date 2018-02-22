package com.springui.web;

import com.springui.ui.DateField;
import com.springui.ui.Field;
import com.springui.ui.UI;
import com.springui.util.WebRequestUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractUIHandler extends AbstractController implements Controller {

    protected WebDataBinder bindUi(UI ui, HttpServletRequest request) {
        ServletRequestDataBinder dataBinder = new ServletRequestDataBinder(ui, "self");
        dataBinder.setAutoGrowNestedPaths(false);
        dataBinder.setAutoGrowCollectionLimit(0);
//        dataBinder.initDirectFieldAccess();
        dataBinder.initBeanPropertyAccess();
        initBinder(ui, dataBinder);
        dataBinder.bind(request);
        return dataBinder;
    }

    protected void initBinder(UI ui, ServletRequestDataBinder webDataBinder) {
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

    protected void initBinder(ServletRequestDataBinder webDataBinder) {
        Object target = webDataBinder.getTarget();
        if (target instanceof UI) {
            initBinder((UI) target, webDataBinder);
        }
    }

    public AbstractUIHandler() {
        setSynchronizeOnSession(true);
//        setRequireSession(true);
    }
}

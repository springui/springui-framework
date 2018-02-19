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
import org.springframework.web.servlet.mvc.Controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractUIController implements Controller {

    protected WebDataBinder bindUi(UI ui, WebRequest request) {
        ServletRequestDataBinder dataBinder = new ServletRequestDataBinder(ui, "self");
        dataBinder.setAutoGrowNestedPaths(false);
        dataBinder.setAutoGrowCollectionLimit(0);
//        dataBinder.initDirectFieldAccess();
        dataBinder.initBeanPropertyAccess();
        initBinder(ui, dataBinder);
        dataBinder.bind(WebRequestUtils.toServletRequest(request));
        return dataBinder;
    }

    protected void initBinder(UI ui, WebDataBinder webDataBinder) {
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

    protected void initBinder(WebDataBinder webDataBinder) {
        Object target = webDataBinder.getTarget();
        if (target instanceof UI) {
            initBinder((UI) target, webDataBinder);
        }
    }
}

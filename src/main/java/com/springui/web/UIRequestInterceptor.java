package com.springui.web;

import com.springui.ui.UIContext;
import com.springui.ui.UIPathMapping;
import com.springui.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
public class UIRequestInterceptor extends HandlerInterceptorAdapter {

//    @Autowired
//    private UIRegistry uiClassRegistry;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            Class<?> controllerClass = method.getBeanType();
            if (UIController.class.isAssignableFrom(controllerClass)) {
                UIController controller = (UIController) method.getBean();

                HttpSession session = request.getSession(false);
//                Class<? extends UI> uiClass = uiClassRegistry.getMapping().find(request);
//                if (uiClass == null) {
//                    session.setAttribute(UI.SESSION_ATTRIBUTE_NAME, null);
//                    return true;
//                }

//                UIPathMapping mapping = AnnotationUtils.findAnnotation(uiClass, UIPathMapping.class);
//                String path = (String) AnnotationUtils.getValue(mapping, "path");

                if (session == null) {
                    session = request.getSession(true);
                }

//                Map<String, UI> uiRegistry = (Map<String, UI>) session.getAttribute("uiRegistry");
//                if (uiRegistry == null) {
//                    uiRegistry = new HashMap<>();
//                    session.setAttribute("uiRegistry", uiRegistry);
//                }

//                UI ui = uiRegistry.get(path);
//                if (ui == null) {
//                    UIContext context = UIContext.forRequest(request);
//                    ui = controller.createUi(uiClass, context);
//                    ui.setPath(path);
//                    ui.init(request);
//                    uiRegistry.put(path, ui);
//                }
//                ui.setPath(path);

//                session.setAttribute(UI.SESSION_ATTRIBUTE_NAME, ui);

                return true;
            }
        }

//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.removeAttribute(UI.SESSION_ATTRIBUTE_NAME);
//        }

        return true;
    }
}

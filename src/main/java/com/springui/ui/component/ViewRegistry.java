package com.springui.ui.component;

import com.springui.web.PathUtils;
import com.springui.web.WebRequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
@Deprecated
public class ViewRegistry {

    private static class ViewRegistration {

        private final String path;
        private final Class<? extends View> viewClass;
        private View view;

        public String getPath() {
            return path;
        }

        public Class<? extends View> getViewClass() {
            return viewClass;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            if (!viewClass.isAssignableFrom(view.getClass())) {
                throw new IllegalArgumentException();
            }

            this.view = view;
        }

        public ViewRegistration(String path, Class<? extends View> viewClass) {
            Assert.notNull(path, "[path] must not be null");
            Assert.notNull(viewClass, "[viewClass] must not be null");

            this.path = path;
            this.viewClass = viewClass;
        }

        public ViewRegistration(String path, View view) {
            this (path, view.getClass());
        }

    }

    private Map<String, ViewRegistration> registrationByPath = new HashMap<>();

    public void registerView(String path, Class<? extends View> viewClass) {
        String normalizedPath = PathUtils.normalize(path);
        ViewRegistration registration = registrationByPath.get(normalizedPath);
        if (registration == null) {
            registration = new ViewRegistration(normalizedPath, viewClass);
            registrationByPath.put(normalizedPath, registration);
        } else {
            throw new RuntimeException("Already registered");
        }
    }

    private View getOrCreateView(String normalizedPath) {
        ViewRegistration registration = registrationByPath.get(normalizedPath);
        if (registration != null) {
            View view = registration.getView();
            if (view == null) {
                Class<? extends View> viewClass = registration.getViewClass();
                view = BeanUtils.instantiate(viewClass);
                registration.setView(view);
            }

            return view;
        }

        return null;
    }

    @Deprecated
    public View navigate(WebRequest request) {
        UI ui = UI.getCurrent();
        String path = WebRequestUtils.getPath(request);
        String prefix = ui.getPath();
        String postfix = StringUtils.removeStart(path, prefix);

        path = PathUtils.normalize(postfix);
        View view = getOrCreateView(path);
        if (view != null) {
            view.setPath(path);
            MultiValueMap<String, String> params =
                    WebRequestUtils.getQueryParams(request);
            view.activate(params);
        } else {
            throw new RuntimeException(String.format("No view registered for path [%s]", path));
        }

        return view;
    }
}

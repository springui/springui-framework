package com.springui.web;

import com.springui.util.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
public class ViewRegistry {

    public static class Registration {

        private final String path;
        private final View view;
        private String originalRequestUrl;

        public String getPath() {
            return path;
        }

        public View getView() {
            return view;
        }

        public String getOriginalRequestUrl() {
            return originalRequestUrl;
        }

        public void setOriginalRequestUrl(String originalRequestUrl) {
            this.originalRequestUrl = originalRequestUrl;
        }

        public Registration(String path, View view) {
            this.path = path;
            this.view = view;
        }
    }

    private final Map<String, Registration> registrations = new HashMap<>();

    public Registration findRegistration(String path) {
        return registrations.get(path);
    }

    public Registration registerView(String path, View view) {
        Registration registration = new Registration(path, view);
        MapUtils.putOnce(registrations, path, registration);
        return registration;
    }
}

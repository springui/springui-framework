package com.springui.web;

import com.springui.ui.View;

/**
 * @author Stephan Grundner
 */
public class ViewMapping {

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
        this.view = view;
    }

    public ViewMapping(String path, Class<? extends View> viewClass) {
        this.path = path;
        this.viewClass = viewClass;
    }
}

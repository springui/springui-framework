package com.springui.web;

import com.springui.ui.UI;

/**
 * @author Stephan Grundner
 */
public class UIMapping {

    private final String path;
    private final Class<? extends UI> uiClass;
    private UI ui;

    public String getPath() {
        return path;
    }

    public Class<? extends UI> getUiClass() {
        return uiClass;
    }

    public UI getUi() {
        return ui;
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }

    public UIMapping(String path, Class<? extends UI> uiClass) {
        this.path = path;
        this.uiClass = uiClass;
    }
}

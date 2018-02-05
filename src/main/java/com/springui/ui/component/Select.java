package com.springui.ui.component;

import java.util.List;

/**
 * @author Stephan Grundner
 */
public abstract class Select<T, V> extends Field<T, V> {

    {setTemplate("ui/select");}

    private List<T> options;

    public List<T> getOptions() {
        return options;
    }

    public void setOptions(List<T> options) {
        this.options = options;
    }
}

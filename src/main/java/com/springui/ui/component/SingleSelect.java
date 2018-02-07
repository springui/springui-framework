package com.springui.ui.component;

import org.springframework.util.StringUtils;

/**
 * @author Stephan Grundner
 */
public class SingleSelect<T> extends Select<T> {

    public static final String DEFAULT = "ui/single-select-default";
    public static final String RADIO_BUTTONS = "ui/single-select-with-radio-buttons";

    {setTemplate(DEFAULT);}

    private String selectedKey;

    public String getSelectedKey() {
        return selectedKey;
    }

    public void setSelectedKey(String selectedKey) {
        this.selectedKey = selectedKey;
    }

    @Override
    public T getValue() {
        if (!StringUtils.isEmpty(selectedKey)) {
            return keyToItem(selectedKey);
        }

        return null;
    }

    @Override
    protected void valueChanged(T newValue, T oldValue) {
        throw new UnsupportedOperationException();
    }
}

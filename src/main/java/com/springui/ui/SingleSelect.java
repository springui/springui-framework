package com.springui.ui;

import org.springframework.util.StringUtils;

/**
 * @author Stephan Grundner
 */
public class SingleSelect<T> extends Select<T> {

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

package com.springui.ui;

import org.springframework.util.StringUtils;

/**
 * @author Stephan Grundner
 * Radio-Buttons
 */
@Template("{theme}/single-select")
public class SingleSelect<T> extends Select<T> {

    private String selectedKey;

    public String getSelectedKey() {
        return selectedKey;
    }

    public void setSelectedKey(String selectedKey) {
        this.selectedKey = selectedKey;
    }

    public T getValue() {
        if (!StringUtils.isEmpty(selectedKey)) {
            return keyToItem(selectedKey);
        }

        return null;
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        visitor.visit(this);
    }
}

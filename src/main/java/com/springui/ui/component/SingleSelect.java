package com.springui.ui.component;

import com.springui.ui.Template;
import org.springframework.util.StringUtils;

/**
 * @author Stephan Grundner
 */
@Template(SingleSelect.DEFAULT)
public class SingleSelect<T> extends Select<T> {

    public static final String DEFAULT = "{theme}/ui/single-select-default";
    public static final String RADIO_BUTTONS = "{theme}/ui/single-select-with-radio-buttons";

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

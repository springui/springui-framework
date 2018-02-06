package com.springui.ui.component;

import org.springframework.util.StringUtils;

/**
 * @author Stephan Grundner
 */
public class SingleSelect<T> extends Select<T> {

    public enum Appearance {
        COMPACT,
        EXPANDED
    }

    private Appearance appearance = Appearance.COMPACT;

    private String selectedKey;

    public Appearance getAppearance() {
        return appearance;
    }

    public void setAppearance(Appearance appearance) {
        this.appearance = appearance;
    }

    @Override
    public String getTemplate() {
        String template = super.getTemplate();
        if (StringUtils.isEmpty(template)) {
            switch (appearance) {
                case EXPANDED:
                    template = "ui/singleselect-expanded";
                    break;
                case COMPACT:
                default:
                    template = "ui/singleselect-compact";
            }
        }

        return template;
    }

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

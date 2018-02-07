package com.springui.ui.component;

import com.springui.collection.CollectionUtils;
import com.springui.ui.Template;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
@Template(MultiSelect.DEFAULT)
public class MultiSelect<T> extends Select<T> {

    public static final String DEFAULT = "{theme}/ui/multi-select-default";
    public static final String CHECK_BOXES = "{theme}/ui/multi-select-with-check-boxes";

    private Set<String> selectedKeys;

    @Override
    public String getTemplate() {
        String template = super.getTemplate();

        if (StringUtils.isEmpty(template)) {
            template = "ui/list-select";
        }

        return template;
    }

    public Set<String> getSelectedKeys() {
        if (selectedKeys == null) {
            selectedKeys = new HashSet<>();
        }

        return selectedKeys;
    }

    public void setSelectedKeys(Set<String> selectedKeys) {
        this.selectedKeys = selectedKeys;
    }

    public Set<T> getValues() {
        Set<T> selection = new LinkedHashSet<>();
        for (String key : getSelectedKeys()) {
            T item = keyToItem(key);
            selection.add(item);
        }

        return selection;
    }

    @Override
    public T getValue() {
        Set<T> values = getValues();
        if (values.size() > 1) {
            throw new IllegalStateException("More than one value available");
        }

        return CollectionUtils.getFirst(getValues());
    }

    @Override
    protected void valueChanged(T newValue, T oldValue) {
        throw new UnsupportedOperationException();
    }
}

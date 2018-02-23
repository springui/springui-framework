package com.springui.ui;

import com.springui.util.CollectionUtils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
public class MultiSelect<T> extends Select<T> {

    private Set<String> selectedKeys;

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

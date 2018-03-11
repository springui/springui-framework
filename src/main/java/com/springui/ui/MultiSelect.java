package com.springui.ui;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/multi-select")
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

    public T getValue() {
        Set<T> values = getValues();
        if (values.size() > 1) {
            throw new IllegalStateException("More than one value available");
        }

        return !values.isEmpty() ? values.iterator().next() : null;
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        visitor.visit(this);
    }
}

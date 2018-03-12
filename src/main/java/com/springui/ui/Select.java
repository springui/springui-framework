package com.springui.ui;

import com.springui.data.DataProvider;
import com.springui.data.ValueReader;
import com.springui.ui.AbstractComponent;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Stephan Grundner
 */
public abstract class Select<T> extends AbstractComponent {

    private DataProvider<T> dataProvider;
//    private ValueReader<T, String> valueReader;

    public DataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    public Collection<T> getItems() {
        return getDataProvider().fetch().collect(Collectors.toList());
    }

    public Object itemToKey(T item) {
        return getDataProvider().keyOf(item);
    }

    public T keyToItem(String key) {
        return getDataProvider().find(key);
    }
}

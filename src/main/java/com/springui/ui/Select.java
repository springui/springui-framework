package com.springui.ui;

import com.springui.data.DataProvider;
import com.springui.data.ItemDisplayValueProvider;
import com.springui.data.ValueResolver;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Stephan Grundner
 */
public abstract class Select<T> extends Field<T> {

    private DataProvider<T> dataProvider;
    private ValueResolver<T, String> valueResolver;

    public DataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Deprecated
    public final ItemDisplayValueProvider<T> getItemDisplayValueProvider() {
        return t -> valueResolver.resolve(t);
    }

    public Collection<T> getItems() {
        return getDataProvider().fetch().collect(Collectors.toList());
    }

    public Object itemToKey(T item) {
        return getDataProvider().getKey(item);
    }

    public T keyToItem(String key) {
        return getDataProvider().getItem(key);
    }
}

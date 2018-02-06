package com.springui.ui.component;

import com.springui.data.DataProvider;
import com.springui.data.ItemDisplayValueProvider;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Stephan Grundner
 */
public abstract class Select<T> extends Field<T> {

    private DataProvider<T> dataProvider;
    private ItemDisplayValueProvider<T> itemDisplayValueProvider;

    public DataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    public ItemDisplayValueProvider<T> getItemDisplayValueProvider() {
        if (itemDisplayValueProvider == null) {
            itemDisplayValueProvider = Objects::toString;
        }

        return itemDisplayValueProvider;
    }

    public void setItemDisplayValueProvider(ItemDisplayValueProvider<T> itemDisplayValueProvider) {
        this.itemDisplayValueProvider = itemDisplayValueProvider;
    }

    public Collection<T> getItems() {
        return getDataProvider().getItems();
    }

    public String itemToKey(T item) {
        return getDataProvider().getKey(item);
    }

    public T keyToItem(String key) {
        return getDataProvider().getItem(key);
    }
}

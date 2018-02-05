package com.springui.ui.component;

import com.springui.data.ItemDisplayValueProvider;
import com.springui.data.ListDataProvider;

import java.util.List;

/**
 * @author Stephan Grundner
 */
public class ComboBox<T> extends Field<Integer> {

    {setTemplate("ui/combobox");}

    private ListDataProvider<T> dataProvider;
    private ItemDisplayValueProvider<T> itemDisplayValueProvider;

    public ListDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(ListDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    public ItemDisplayValueProvider<T> getItemDisplayValueProvider() {
        return itemDisplayValueProvider;
    }

    public void setItemDisplayValueProvider(ItemDisplayValueProvider<T> itemDisplayValueProvider) {
        this.itemDisplayValueProvider = itemDisplayValueProvider;
    }

    public List<T> getItems() {
        return dataProvider.getItems();
    }

    public void setItems(List<T> items) {
        setDataProvider(new ListDataProvider<T>() {
            @Override
            public List<T> getItems() {
                return items;
            }
        });
    }
}

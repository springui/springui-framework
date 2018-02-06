package com.springui.data;

import java.util.Collection;
import java.util.EnumSet;

/**
 * @author Stephan Grundner
 */
public class EnumDataProvider<E extends Enum<E>> implements DataProvider<E> {

    private final Class<E> enumType;
    private Collection<E> items;

    @Override
    public Collection<E> getItems() {
        if (items == null) {
            items = EnumSet.allOf(enumType);
        }

        return items;
    }

    @Override
    public String getKey(E e) {
        return e.name();
    }

    @Override
    public E getItem(String key) {
        return Enum.valueOf(enumType, key);
    }

    public EnumDataProvider(Class<E> enumType, Collection<E> items) {
        this.enumType = enumType;
        this.items = items;
    }

    public EnumDataProvider(Class<E> enumType, E first, E... rest) {
        this(enumType, EnumSet.of(first, rest));
    }

    public EnumDataProvider(Class<E> enumType) {
        this(enumType, null);
    }
}

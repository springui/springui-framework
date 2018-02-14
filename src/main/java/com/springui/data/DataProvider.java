package com.springui.data;

import org.springframework.util.Assert;

import java.util.Collection;

/**
 * @author Stephan Grundner
 */
public interface DataProvider<T> {

    @Deprecated
    Collection<T> getItems();



    default Object getKey(T t) {
        Assert.notNull(t);
        return t;
    }

    T getItem(Object key);
}

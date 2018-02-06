package com.springui.data;

import java.util.Collection;

/**
 * @author Stephan Grundner
 */
public interface DataProvider<T> {

    Collection<T> getItems();

    String getKey(T t);
    T getItem(String key);
}

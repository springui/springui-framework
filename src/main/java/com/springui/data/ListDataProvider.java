package com.springui.data;

import java.util.List;

/**
 * @author Stephan Grundner
 */
public interface ListDataProvider<T> extends DataProvider<T> {

    List<T> getItems();
}

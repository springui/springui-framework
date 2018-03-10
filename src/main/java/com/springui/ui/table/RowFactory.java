package com.springui.ui.table;

import com.springui.ui.Table;

/**
 * @author Stephan Grundner
 */
public interface RowFactory<T> {

    Row<T> createRow(Table<T> table);
}

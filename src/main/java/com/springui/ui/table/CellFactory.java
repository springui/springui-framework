package com.springui.ui.table;

import com.springui.ui.Component;

/**
 * @author Stephan Grundner
 */
public interface CellFactory<T> {

    Component createCell(Column<T, ?> column, Row<T> row);
}

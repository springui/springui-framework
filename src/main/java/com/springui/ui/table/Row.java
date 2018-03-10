package com.springui.ui.table;

import com.springui.ui.*;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/table-row")
public class Row<T> extends AbstractComponent {

    private final Table<T> table;
    private T object;

    private final Map<Column<T, ?>, Component> cellByColumn = new IdentityHashMap<>();

    public Table<T> getTable() {
        return table;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public Component getCell(Column<T, ?> column) {
        Component cell = cellByColumn.get(column);
        if (cell == null) {
            CellFactory<T> cellFactory = column.getCellFactory();
            cell = cellFactory.createCell(column, Row.this);
            cellByColumn.put(column, cell);
        }

        return cell;
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        visitor.visit(this);
        cellByColumn.values().forEach(cell -> cell.walk(visitor));
    }

    public Row(Table<T> table) {
        this.table = table;
    }
}

package com.springui.ui.component;

import com.springui.data.Binding;
import com.springui.data.ListDataProvider;

import java.util.*;

/**
 * @author Stephan Grundner
 */
public class Table<T> extends Component {

    public static class Column<T, V> extends Component {

        private Binding<T, V> binding;

        public Binding<T, V> getBinding() {
            return binding;
        }

        public void setBinding(Binding<T, V> binding) {
            this.binding = binding;
        }
    }

    public static class Cell<T, V> extends Component {

        private Column<T ,V> column;
        private Row<T> row;

        public Column<T, V> getColumn() {
            return column;
        }

        public void setColumn(Column<T, V> column) {
            this.column = column;
        }

        public Row<T> getRow() {
            return row;
        }

        public void setRow(Row<T> row) {
            this.row = row;
        }

        public V getValue() {
            Binding<T, V> binding = column.getBinding();
            T object = row.getObject();
            try {
                binding.setObject(object);
                binding.fromObject();
                return binding.getValue();
            } finally {
                binding.setObject(null);
            }
        }

    }

    public static class Row<T> extends Component {

        private T object;
        private final Map<Column<T, ?>, Cell<T, ?>> cellByColumn = new IdentityHashMap<>();

        public Table<T> getTable() {
            return (Table<T>) getParent();
        }

        public T getObject() {
            return object;
        }

        public void setObject(T object) {
            this.object = object;
        }

        public <V> Cell<T, ?> getCell(Column<T, V> column) {
            Cell<T, V> cell = (Cell<T, V>) cellByColumn.get(column);
            if (cell == null) {
                cell = new Cell<>();
                cell.setColumn(column);
                cell.setRow(this);
                cellByColumn.put(column, cell);
            }

            return cell;
        }
    }

    { setTemplate("ui/table"); }

    private final List<Column<T, ?>> columns = new ArrayList<>();
    private final List<Row<T>> rows = new ArrayList<>();
    private ListDataProvider<T> dataProvider;

    public List<Column<T, ?>> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        super.walk(visitor);
        for (Column<T, ?> column : getColumns()) {
            column.walk(visitor);
        }
        for (Row<T> row : getRows()) {
            row.walk(visitor);
        }
    }

    public <V> Column<T, V> addColumn() {
        Column<T, V> column = new Column<>();
        columns.add(column);
        column.setParent(this);
        return column;
    }

    private Row<T> addRow(T object) {
        Row<T> row = new Row<>();
        row.setObject(object);
        rows.add(row);
        row.setParent(this);
        return row;
    }

    public List<Row<T>> getRows() {
        ListDataProvider<T> dataProvider = getDataProvider();
        rows.clear();
        ListIterator<T> objectItr = dataProvider.getObjects().listIterator();
        while (objectItr.hasNext()) {
            addRow(objectItr.next());
        }

        return rows;
    }

    public ListDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(ListDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }
}

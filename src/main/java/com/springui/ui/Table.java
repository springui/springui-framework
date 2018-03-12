package com.springui.ui;

import com.springui.data.DataProvider;
import com.springui.data.ValueReader;
import com.springui.i18n.Message;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/table")
public class Table<T> extends AbstractComponent {

    public interface CellFactory<T> {

        Component createCell(Column<T, ?> column, Row<T> row);
    }

    @Template("{theme}/table-column")
    public static class Column<T, V> extends AbstractComponent {

        public enum Alignment {
            LEFT,
            CENTER,
            RIGHT
        }

        private ValueReader<T, V> valueResolver;
        private CellFactory<T> cellFactory;
        private Alignment alignment = Alignment.LEFT;
        private int ordinal;

        public ValueReader<T, V> getValueResolver() {
            return valueResolver;
        }

        public void setValueResolver(ValueReader<T, V> valueResolver) {
            this.valueResolver = valueResolver;
        }

        public CellFactory<T> getCellFactory() {
            if (cellFactory == null) {
                cellFactory = (column, row) -> {
                    Text cell = new Text();
                    ValueReader<T, ?> valueResolver = column.getValueResolver();
                    if (valueResolver != null) {
                        Object value = valueResolver.read(row.getObject());
                        cell.setMessage(new Message((String) null, value.toString()));
                    }

                    return cell;
                };
            }

            return cellFactory;
        }

        public void setCellFactory(CellFactory<T> cellFactory) {
            this.cellFactory = cellFactory;
        }

        public Alignment getAlignment() {
            return alignment;
        }

        public void setAlignment(Alignment alignment) {
            this.alignment = alignment;
        }

        public int getOrdinal() {
            return ordinal;
        }

        public void setOrdinal(int ordinal) {
            this.ordinal = ordinal;
        }

        public void walk(ComponentVisitor visitor) {
            visitor.visit(this);
        }
    }

    @Template("{theme}/table-row")
    public static class Row<T> extends AbstractComponent {

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

    public interface RowFactory<T> {

        Row<T> createRow(Table<T> table);
    }

    private final List<Column<T, ?>> columns = new ArrayList<>();

    private RowFactory<T> rowFactory;
    private final List<Row<T>> rows = new ArrayList<>();

    private DataProvider<T> dataProvider;

    public List<Column<T, ?>> getColumns() {
        List<Column<T, ?>> list = columns.stream()
                .sorted(Comparator.comparingInt(Column::getOrdinal))
                .collect(Collectors.toList());

        return Collections.unmodifiableList(list);
    }

    public <V> Column<T, V> addColumn(Message caption, ValueReader<T, V> valueResolver) {
        Column<T, V> column = new Column<>();
        column.setOrdinal(columns.size());
        column.setCaption(caption);
        column.setValueResolver(valueResolver);
        column.setParent(this);

        boolean successful = columns.add(column);
        Assert.isTrue(successful);

        return column;
    }

    public RowFactory<T> getRowFactory() {
        if (rowFactory == null) {
            rowFactory = Row::new;
        }

        return rowFactory;
    }

    public void setRowFactory(RowFactory<T> rowFactory) {
        this.rowFactory = rowFactory;
    }

    public void removeRow(int index) {
        Row<T> row = rows.remove(index);
        row.setParent(null);
    }

    private void appendRow(T object) {
        Row<T> row = getRowFactory().createRow(this);
        row.setObject(object);
        row.setParent(this);
        if (!rows.add(row)) {
            throw new IllegalStateException();
        }
    }

    public List<Row<T>> getRows() {
        while (rows.size() > 0) {
            removeRow(0);
        }

        DataProvider<T> dataProvider = getDataProvider();
        if (dataProvider != null) {
            dataProvider.fetch().forEach(this::appendRow);
        }

        return Collections.unmodifiableList(rows);
    }

    public DataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        for (Row<T> row : rows) {
            row.walk(visitor);
        }
    }
}

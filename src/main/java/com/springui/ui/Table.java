package com.springui.ui;

import com.springui.data.DataProvider;
import com.springui.data.ValueResolver;
import com.springui.i18n.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Stephan Grundner
 */
public class Table<T> extends Component {

    public interface CellComponentProvider<T, V> {

        Component getCellComponent(Cell<T, V> cell);
    }

    public static class Column<T, V> extends Component {

        public enum Alignment {
            LEFT,
            CENTER,
            RIGHT
        }

        private Message caption;
        private ValueResolver<T, V> valueResolver;
        private CellComponentProvider<T, V> cellComponentProvider;

        private Alignment alignment = Alignment.LEFT;
        private int ordinal;

        @Override
        public Message getCaption() {
            return caption;
        }

        @Override
        public void setCaption(Message caption) {
            this.caption = caption;
        }

        public ValueResolver<T, V> getValueResolver() {
            return valueResolver;
        }

        public void setValueResolver(ValueResolver<T, V> valueResolver) {
            this.valueResolver = valueResolver;
        }

        public CellComponentProvider<T, V> getCellComponentProvider() {
            if (cellComponentProvider == null) {
                cellComponentProvider = new CellComponentProvider<T, V>() {
                    @Override
                    public Component getCellComponent(Cell<T, V> cell) {
                        return new Text("", cell.getValueAsText());
                    }
                };
            }
            return cellComponentProvider;
        }

        public void setCellComponentProvider(CellComponentProvider<T, V> cellComponentProvider) {
            this.cellComponentProvider = cellComponentProvider;
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

        public <X> X getValue(Class<X> valueType) {
            Column<T, V> column = getColumn();
            ValueResolver<T, V> valueResolver = column.getValueResolver();
            if (valueResolver != null) {
                Row<T> row = getRow();
                T object = row.getObject();

                ApplicationContext applicationContext = getUi().getApplicationContext();
                ConversionService conversionService = applicationContext.getBean(ConversionService.class);
                V value = valueResolver.resolve(object);

                return conversionService.convert(value, valueType);
            }

            return null;
        }

        public String getValueAsText() {
            return getValue(String.class);
        }

        public Component getComponent() {
            CellComponentProvider<T, V> componentProvider = getColumn().getCellComponentProvider();

            Component component = componentProvider.getCellComponent(this);
            component.setParent(this);
            return component;
        }
    }

    public static class Row<T> extends Component {

        private T object;
        private final Map<Column<T, ?>, Cell<T, ?>> cellByColumn = new IdentityHashMap<>();

        @SuppressWarnings("unchecked")
        public Table<T> getTable() {
            return (Table<T>) getParent();
        }

        public T getObject() {
            return object;
        }

        public void setObject(T object) {
            this.object = object;
        }

        public Collection<Cell<T, ?>> getCells() {
            return Collections.unmodifiableCollection(cellByColumn.values());
        }

        public <V> Cell<T, V> getCell(Column<T, V> column) {
            Cell<T, V> cell = (Cell<T, V>) cellByColumn.get(column);
            if (cell == null) {
                cell = new Cell<>();
                cell.setColumn(column);
                cell.setRow(this);
                cell.setParent(this);
                cellByColumn.put(column, cell);
            }

            return cell;
        }

//        public void removeCell(Column<T, ?> column) {
//            Cell<T, ?> cell = cellByColumn.remove(column);
//            if (cell != null) {
//                cell.setParent(null);
//            }
//        }

        public void removeAllCells() {
            Iterator<Cell<T, ?>> i = cellByColumn.values().iterator();
            while (i.hasNext()) {
                Cell<T, ?> cell = i.next();
                cell.setParent(null);
                i.remove();
            }
        }
    }

    private final List<Column<T, ?>> columns = new ArrayList<>();
    private final List<Row<T>> rows = new ArrayList<>();
    private DataProvider<T> dataProvider;

    private int maximum = Integer.MAX_VALUE;

    public List<Column<T, ?>> getColumns() {
        List<Column<T, ?>> result = columns.stream()
                .sorted(Comparator.comparingInt(Column::getOrdinal))
                .collect(Collectors.toList());

        return Collections.unmodifiableList(result);
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

    public <V> Column<T, V> addColumn(Message caption, ValueResolver<T, V> valueResolver, CellComponentProvider<T, V> cellComponentProvider) {
        Column<T, V> column = new Column<>();
        column.setCaption(caption);
        column.setValueResolver(valueResolver);
        column.setCellComponentProvider(cellComponentProvider);
        column.setOrdinal(getColumns().size());
        column.setParent(this);

        boolean successful = columns.add(column);
        Assert.isTrue(successful);

        return column;
    }

    public <V> Column<T, V> addColumn(Message caption, ValueResolver<T, V> valueResolver) {
        return addColumn(caption, valueResolver, null);
    }

    public <V> Column<T, V> addColumn(Message caption) {
        return addColumn(caption, null, null);
    }

    public <V> Column<T, V> addColumn() {
        return addColumn(null);
    }

    private void appendRow(T object) {
        Row<T> row = new Row<>();
        row.setObject(object);
        rows.add(row);
        row.setParent(this);
    }

    private Row<T> removeRow(int index) {
        Row<T> row = rows.remove(index);
        row.setParent(null);
        row.removeAllCells();
        return row;
    }

    private void removeAllRows() {
        while (rows.size() > 0) {
            Row<T> row = removeRow(0);
//            TODO LOG row removed
        }
    }

    public List<Row<T>> getRows() {
        removeAllRows();

        DataProvider<T> dataProvider = getDataProvider();
        if (dataProvider != null) {
            dataProvider.fetch(0, getMaximum()).forEach(this::appendRow);
        }

        return rows;
    }

    public DataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }
}

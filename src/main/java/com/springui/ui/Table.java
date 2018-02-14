package com.springui.ui;

import com.springui.data.DataProvider;
import com.springui.data.ValueResolver;
import com.springui.i18n.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;

import java.util.*;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/table")
public class Table<T> extends Component {

    public interface CellComponentProvider<T, V> {

        Component getCellComponent(Cell<T, V> cell);
    }

    public static class Column<T, V> extends Component {

        private Message caption;
        private ValueResolver<T, V> valueResolver;
        private CellComponentProvider<T, V> cellComponentProvider;

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

        public Table<T> getTable() {
            return (Table<T>) getParent();
        }

        public T getObject() {
            return object;
        }

        public void setObject(T object) {
            this.object = object;
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
    }

    private final List<Column<T, ?>> columns = new ArrayList<>();
    private final List<Row<T>> rows = new ArrayList<>();
    private DataProvider<T> dataProvider;

    private int maximum = 3;

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

    public <V> Column<T, V> addColumn(Message caption) {
        Column<T, V> column = addColumn();
        column.setCaption(caption);

        return column;
    }

    public <V> Column<T, V> addColumn(Message caption, ValueResolver<T, V> valueResolver) {
        Column<T, V> column = addColumn();
        column.setCaption(caption);
        column.setValueResolver(valueResolver);

        return column;
    }

    public <V> Column<T, V> addColumn(Message caption, ValueResolver<T, V> valueResolver, CellComponentProvider<T, V> cellComponentProvider) {
        Column<T, V> column = addColumn(caption, valueResolver);
        column.setCellComponentProvider(cellComponentProvider);

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
        DataProvider<T> dataProvider = getDataProvider();
        rows.clear();
        dataProvider.fetch(0, getMaximum()).forEach(object -> {
            addRow(object);
        });

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

package com.springui.ui;

import com.springui.data.DataProvider;
import com.springui.data.ValueReader;
import com.springui.i18n.Message;
import com.springui.ui.table.*;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/table")
public class Table<T> extends AbstractComponent {

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

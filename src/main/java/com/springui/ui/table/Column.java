package com.springui.ui.table;

import com.springui.data.ValueReader;
import com.springui.i18n.Message;
import com.springui.ui.*;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/table-column")
public class Column<T, V> extends AbstractComponent {

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

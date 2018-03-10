package com.springui.data;

import com.springui.ui.Field;
import org.springframework.util.Assert;

/**
 * @author Stephan Grundner
 */
public class DataBinding<T, V> {

    private final DataBinder<T> binder;

    private ValueReader<T, V> reader;
    private ValueWriter<T, V> writer;
    private Field<V> field;

    public ValueReader<T, V> getReader() {
        return reader;
    }

    public void setReader(ValueReader<T, V> reader) {
        this.reader = reader;
    }

    public ValueWriter<T, V> getWriter() {
        return writer;
    }

    public void setWriter(ValueWriter<T, V> writer) {
        this.writer = writer;
    }

    public Field<V> getField() {
        return field;
    }

    protected void read() {
        T object = binder.getObject();
        if (object == null) {
            throw new IllegalStateException();
        }

        V value = reader.read(object);
        field.setValue(value);
    }

    protected void write() {
        T object = binder.getObject();
        if (object == null) {
            throw new IllegalStateException();
        }

        V value = field.getValue();
        writer.write(object, value);
    }

    public boolean isReadOnly() {
        return writer == null;
    }

    public DataBinding(DataBinder<T> binder) {
        Assert.notNull(binder, "[binder] must not be null!");
        this.binder = binder;
    }

    public DataBinding(DataBinder<T> binder, Field<V> field) {
        this(binder);
        Assert.notNull(field, "[field] must not be null!");
        this.field = field;
    }
}

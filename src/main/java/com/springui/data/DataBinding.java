package com.springui.data;

import com.springui.event.ValueChangeListener;
import com.springui.ui.Field;

/**
 * @author Stephan Grundner
 */
public class DataBinding<T, V> {

    private final DataBinder<T> binder;

    private ValueResolver<T, V> resolver;
    private ValueApplier<T, V> applier;
    private Field<V> field;

    private ValueChangeListener<V> changeListener;

    public ValueResolver<T, V> getResolver() {
        return resolver;
    }

    public void setResolver(ValueResolver<T, V> resolver) {
        this.resolver = resolver;
    }

    public ValueApplier<T, V> getApplier() {
        return applier;
    }

    public void setApplier(ValueApplier<T, V> applier) {
        this.applier = applier;
    }

    public Field<V> getField() {
        return field;
    }

    public ValueChangeListener<V> getChangeListener() {
        return changeListener;
    }

    protected void setChangeListener(ValueChangeListener<V> changeListener) {
        this.changeListener = changeListener;
    }

    protected void resolve() {
        T object = binder.getObject();
        if (object == null) {
            throw new IllegalStateException();
        }

        V value = resolver.resolve(object);
        field.setValue(value);
    }

    protected void apply() {
        T object = binder.getObject();
        if (object == null) {
            throw new IllegalStateException();
        }

        V value = field.getValue();
        applier.apply(object, value);
    }


    public DataBinding(DataBinder<T> binder, Field<V> field) {
        this.binder = binder;
        this.field = field;
    }
}

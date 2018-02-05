package com.springui.data;

import com.springui.event.ValueChangeListener;
import com.springui.ui.component.Field;

/**
 * @author Stephan Grundner
 */
public class DataBinder<O, T> {

    private ValueApplier<O, T> applier;
    private ValueResolver<O, T> resolver;
    private Field<T> field;
    private O object;

    private ValueChangeListener<T> valueChangeListener;

    public ValueApplier<O, T> getApplier() {
        return applier;
    }

    public void setApplier(ValueApplier<O, T> applier) {
        this.applier = applier;
    }

    public ValueResolver<O, T> getResolver() {
        return resolver;
    }

    public void setResolver(ValueResolver<O, T> resolver) {
        this.resolver = resolver;
    }

    public Field<T> getField() {
        return field;
    }

    public O getObject() {
        return object;
    }

    public void setObject(O object) {
        this.object = object;
    }

    public void apply() {
        T value = field.getValue();
        applier.apply(object, value);
    }

    public void resolve() {
        T value = resolver.resolve(object);
        field.setValue(value);
    }

    public void bind(Field<T> field, ValueApplier<O, T> applier, ValueResolver<O, T> resolver, O object) {
        this.field = field;
        this.applier = applier;
        this.resolver = resolver;
        this.object = object;
        valueChangeListener = change -> apply();
        field.addValueChangeListener(valueChangeListener);
        resolve();
    }

    public void unbind() {
        field.removeValueChangeListener(valueChangeListener);
        field = null;
    }
}

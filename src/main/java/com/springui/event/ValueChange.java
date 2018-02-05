package com.springui.event;

import com.springui.ui.component.Field;

/**
 * @author Stephan Grundner
 */
public final class ValueChange<T> {

    private Field<T> field;
    private T oldValue;

    public Field<T> getField() {
        return field;
    }

    public T getOldValue() {
        return oldValue;
    }

    public ValueChange(Field<T> field, T oldValue) {
        this.field = field;
        this.oldValue = oldValue;
    }
}

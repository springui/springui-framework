package com.springui.ui.field;

import com.springui.ui.AbstractComponent;
import com.springui.ui.Field;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractField<T> extends AbstractComponent implements Field<T> {

    private final Set<ValueChangeListener<T>> valueChangeListeners = new LinkedHashSet<>();

    public abstract T getValue();

    protected abstract void changeValue(T value);

    private void notifyValueChanged(ValueChange<T> change) {
        for (ValueChangeListener<T> valueChangeListener : valueChangeListeners) {
            valueChangeListener.valueChanged(change);
        }
    }

    @Override
    public void setValue(T value) {
        T currentValue = getValue();

        if (!Objects.equals(getValue(), value)) {
            changeValue(value);
            notifyValueChanged(new ValueChange<>(this, currentValue));
        }
    }

    public boolean addValueChangeListener(ValueChangeListener<T> valueChangeListener) {
        return valueChangeListeners.add(valueChangeListener);
    }

    public boolean removeValueChangeListener(ValueChangeListener<T> valueChangeListener) {
        return valueChangeListeners.remove(valueChangeListener);
    }
}

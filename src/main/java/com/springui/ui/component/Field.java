package com.springui.ui.component;

import com.springui.event.ValueChange;
import com.springui.event.ValueChangeListener;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
public abstract class Field<T> extends Component {

    private final Set<ValueChangeListener<T>> valueChangeListeners = new LinkedHashSet<>();

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        T oldValue = this.value;
        this.value = value;

        if (!Objects.equals(oldValue, value)) {
            notifyValueChanged(new ValueChange<>(this, oldValue));
        }
    }

    private void notifyValueChanged(ValueChange<T> change) {
        for (ValueChangeListener<T> valueChangeListener : valueChangeListeners) {
            valueChangeListener.valueChanged(change);
        }
    }

    public boolean addValueChangeListener(ValueChangeListener<T> valueChangeListener) {
        return valueChangeListeners.add(valueChangeListener);
    }

    public boolean removeValueChangeListener(ValueChangeListener<T> valueChangeListener) {
        return valueChangeListeners.remove(valueChangeListener);
    }

    public Field() { }
}

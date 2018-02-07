package com.springui.ui.component;

import com.springui.event.ValueChange;
import com.springui.event.ValueChangeListener;
import com.springui.i18n.Message;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
public abstract class Field<T> extends Component {

    private Message placeholder;
    private Message help;

    private final Set<ValueChangeListener<T>> valueChangeListeners = new LinkedHashSet<>();

    public Message getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(Message placeholder) {
        this.placeholder = placeholder;
    }

    public Message getHelp() {
        return help;
    }

    public void setHelp(Message help) {
        this.help = help;
    }

    public abstract T getValue();

    protected abstract void valueChanged(T newValue, T oldValue);

    public void setValue(T value) {
        T oldValue = getValue();

        if (!Objects.equals(oldValue, value)) {
            valueChanged(value, oldValue);
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

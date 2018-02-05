package com.springui.ui.component;

import com.springui.data.Binding;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Stephan Grundner
 */
public abstract class Field<T, V> extends Component {

    private Binding<T, V> binding;

    private Binding<T, V> getBinding() {
        if (binding == null) {
            binding = new Binding<>();
        }

        return binding;
    }

    public V getValue() {
        return getBinding().getValue();
    }

    public void setValue(V value) {
        getBinding().setValue(value);
    }

    public void setObject(T object) {
        getBinding().setObject(object);
    }

    public Field() { }

    public Field(Binding<T, V> binding) {
        this.binding = binding;
    }

    public Field(Function<T, V> getter, BiConsumer<T, V> setter) {
        this(new Binding<>(getter, setter));
    }

    public Field(T object, Function<T, V> getter, BiConsumer<T, V> setter) {
        this(getter, setter);
        binding.setObject(object);
    }
}

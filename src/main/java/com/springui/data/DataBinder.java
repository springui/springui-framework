package com.springui.data;

import com.springui.ui.Field;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
public class DataBinder<T> {

    private T object;
    private final Set<DataBinding<T, ?>> bindings = new LinkedHashSet<>();

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;

        fromObject();
    }

    public <V> DataBinding<T, V> bind(Field<V> field, ValueResolver<T, V> resolver, ValueApplier<T, V> applier) {
        DataBinding<T, V> binding = new DataBinding<>(this, field);
        binding.setResolver(resolver);
        binding.setApplier(applier);

        binding.setChangeListener(change -> binding.apply());
        field.addValueChangeListener(binding.getChangeListener());

        bindings.add(binding);

        if (object != null) {
            binding.resolve();
        }

        return binding;
    }

    public <V> boolean unbind(DataBinding<T, V> binding) {
        if (bindings.remove(binding)) {
            Field<V> field = binding.getField();
            field.removeValueChangeListener(binding.getChangeListener());
            binding.setChangeListener(null);

            return true;
        }

        return false;
    }

    public void fromObject() {
        bindings.forEach(DataBinding::resolve);
    }

    public void toObject() {
        bindings.forEach(DataBinding::apply);
    }

    public DataBinder(T object) {
        this.object = object;
    }

    public DataBinder() { }
}

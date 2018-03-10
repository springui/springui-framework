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

    public <V> DataBinding<T, V> bind(Field<V> field, ValueReader<T, V> reader, ValueWriter<T, V> writer) {
        DataBinding<T, V> binding = new DataBinding<>(this, field);
        binding.setReader(reader);
        binding.setWriter(writer);

//        binding.setChangeListener(change -> binding.apply());
//        field.addValueChangeListener(binding.getChangeListener());

        bindings.add(binding);

        if (object != null) {
            binding.read();
        }

        return binding;
    }

    public <V> DataBinding<T, V> bind(Field<V> field, ValueReader<T, V> reader) {
        return bind(field, reader, null);
    }

    public <V> boolean unbind(DataBinding<T, V> binding) {
        if (bindings.remove(binding)) {
            Field<V> field = binding.getField();
//            field.removeValueChangeListener(binding.getChangeListener());
//            binding.setChangeListener(null);

            return true;
        }

        return false;
    }

    public void fromObject() {
        bindings.forEach(DataBinding::read);
    }

    public void toObject() {
        bindings.forEach(DataBinding::write);
    }

    public DataBinder(T object) {
        this.object = object;
    }

    public DataBinder() { }
}

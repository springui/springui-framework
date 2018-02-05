package com.springui.ui.component;

import com.springui.data.Binding;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Stephan Grundner
 */
public class BooleanField<T> extends Field<T, Boolean> {

    {
        setTemplate("ui/boolean-field");
    }

    public BooleanField() { }

    public BooleanField(Binding<T, Boolean> binding) {
        super(binding);
    }

    public BooleanField(Function<T, Boolean> getter, BiConsumer<T, Boolean> setter) {
        super(getter, setter);
    }

    public BooleanField(T object, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter) {
        super(object, getter, setter);
    }
}

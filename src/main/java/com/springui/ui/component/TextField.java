package com.springui.ui.component;

import com.springui.data.Binding;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Stephan Grundner
 */
public class TextField<T> extends Field<T, String> {

    {
        setTemplate("ui/text-field");
    }

    public TextField() { }

    public TextField(Binding<T, String> binding) {
        super(binding);
    }

    public TextField(Function<T, String> getter, BiConsumer<T, String> setter) {
        super(getter, setter);
    }

    public TextField(T object, Function<T, String> getter, BiConsumer<T, String> setter) {
        super(object, getter, setter);
    }
}

package com.springui.data;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Stephan Grundner
 */
public class Binding<T, V> {

    private Function<T, V> getter;
    private BiConsumer<T, V> setter;

    private T object;
    private V value;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void toObject() {
        setter.accept(object, value);
    }

    public void fromObject() {
        value = getter.apply(object);
    }

    public Binding() { }

    public Binding(Function<T, V> getter) {
        this.getter = getter;
    }

    public Binding(Function<T, V> getter, BiConsumer<T, V> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public Binding(T object, Function<T, V> getter, BiConsumer<T, V> setter) {
        this(getter, setter);
        this.object = object;
    }
}

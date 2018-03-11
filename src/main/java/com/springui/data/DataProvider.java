package com.springui.data;

import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Stephan Grundner
 */
public interface DataProvider<T> {

    Stream<T> fetch();

    default Object keyOf(T t) {
        Assert.notNull(t);
        return t;
    }

    default T find(Object key) {
        return fetch()
                .filter(it -> Objects.equals(keyOf(it), key))
                .findFirst()
                .orElse(null);
    }
}

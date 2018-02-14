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

    default Stream<T> fetch(int skip, int limit) {
        return fetch().skip(skip).limit(limit);
    }

    default Object getKey(T t) {
        Assert.notNull(t);
        return t;
    }

    default T getItem(Object key) {
        return fetch().filter(it -> Objects.equals(getKey(it), key))
                .findFirst().
                        orElse(null);
    }
}

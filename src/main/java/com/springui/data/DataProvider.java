package com.springui.data;

import org.springframework.util.Assert;

import java.util.Collection;
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
}

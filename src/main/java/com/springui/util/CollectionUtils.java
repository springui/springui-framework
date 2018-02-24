package com.springui.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Stephan Grundner
 */
public final class CollectionUtils {

    public static <E> E getFirst(Collection<E> collection) {
        Iterator<E> i = collection.iterator();
        return i.hasNext() ? i.next() : null;
    }

    public static <E> E getFirst(Iterable<E> iterable) {
        if (iterable != null) {
            Iterator<E> iterator = iterable.iterator();
            return iterator.hasNext() ? iterator.next() : null;
        }

        return null;
    }

    private CollectionUtils() {}
}

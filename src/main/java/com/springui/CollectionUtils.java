package com.springui;

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

    private CollectionUtils() {}
}

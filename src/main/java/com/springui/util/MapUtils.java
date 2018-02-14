package com.springui.util;

import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;

/**
 * @author Stephan Grundner
 */
public final class MapUtils {

    public static <K, V> void putValueOnce(Map<K, V> map, K key, V value) {
        if (map.containsKey(key)) {
            throw new IllegalStateException(String.format("Key [%s] already exists", key));
        }

        V replaced = map.put(key, value);
        Assert.isNull(replaced, String.format("Value for key [%s] was replaced", key));
    }

    public static <K, V> K findKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }

        return null;
    }

    private MapUtils() {}
}

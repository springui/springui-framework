package com.springui.util;

import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author Stephan Grundner
 */
public final class MapUtils {

    public static <K, V> void putOnce(Map<K, V> map, K key, V value) {
        if (map.containsKey(key)) {
            throw new IllegalStateException(String.format("Key [%s] already exists", key));
        }

        V replaced = map.put(key, value);
        Assert.isNull(replaced, String.format("Value for key [%s] was replaced", key));
    }

    public static <K, V> MultiValueMap<K, V> toMap(K key, V[] values) {
        MultiValueMap<K, V> map = new LinkedMultiValueMap<>(values.length);
        for (V value : values) {
            map.add(key, value);
        }

        return map;
    }

    private MapUtils() {}
}

package com.springui.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
public class MapBuilder<K, V> {

    public static <K, V> MapBuilder<K, V> createHashMap() {
        return new MapBuilder<>(new HashMap<>());
    }

    private final Map<K, V> map;

    public MapBuilder<K, V> put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public Map<K, V> getMap() {
        return map;
    }

    public Map<K, V> getUnmodifiableMap() {
        return Collections.unmodifiableMap(map);
    }

    public MapBuilder(Map<K, V> map) {
        this.map = map;
    }
}

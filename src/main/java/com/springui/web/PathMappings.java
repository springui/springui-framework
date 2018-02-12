package com.springui.web;

import com.springui.util.PathUtils;
import com.springui.util.WebRequestUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Stephan Grundner
 */
public class PathMappings<T> implements Map<String, T> {

    private final Map<String, T> map;
    private final UrlPathHelper pathHelper = new UrlPathHelper();

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public T get(Object key) {
        return map.get(key);
    }

    public T find(String path) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        List<String> paths = new ArrayList<>(keySet());
        paths.sort(Comparator.comparingInt(String::length).reversed());
        for (String pattern : paths) {
            path = PathUtils.normalize(path);
            if (pathMatcher.match(String.format("%s/**", pattern), path)) {
                return get(pattern);
            }
        }

        return null;
    }

    public T find(HttpServletRequest request) {
        String path = pathHelper.getPathWithinApplication(request);
        return find(path);
    }

    public T find(WebRequest request) {
        return find(WebRequestUtils.toServletRequest(request));
    }

    @Override
    public T put(String key, T value) {
        String path = PathUtils.normalize(key);
        return map.put(path, value);
    }

    @Override
    public T remove(Object key) {
        return map.remove(key);
    }

    @Override
    public final void putAll(Map<? extends String, ? extends T> m) {
        for (Map.Entry<? extends String, ? extends T> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(map.keySet());
    }

    @Override
    public Collection<T> values() {
        return Collections.unmodifiableCollection(map.values());
    }

    @Override
    public Set<Entry<String, T>> entrySet() {
        return Collections.unmodifiableSet(map.entrySet());
    }

    public PathMappings(Map<String, T> map) {
        this.map = map;
    }

    public PathMappings() {
        this(new HashMap<>());
    }
}

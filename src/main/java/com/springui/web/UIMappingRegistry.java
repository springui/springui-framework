package com.springui.web;

import com.springui.ui.UI;
import com.springui.util.MapUtils;
import com.springui.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Stephan Grundner
 */
public class UIMappingRegistry {

    public static class Mapping {

        private final String pattern;
        private final Class<? extends UI> uiClass;

        public String getPattern() {
            return pattern;
        }

        public Class<? extends UI> getUiClass() {
            return uiClass;
        }

        public Mapping(String pattern, Class<? extends UI> uiClass) {
            this.pattern = pattern;
            this.uiClass = uiClass;
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(UIMappingRegistry.class);

    private UrlPathHelper pathHelper;

    private final Map<String, Mapping> mappings = new HashMap<>();

    public UrlPathHelper getPathHelper() {
        if (pathHelper == null) {
            pathHelper = new UrlPathHelper();
        }

        return pathHelper;
    }

    public void setPathHelper(UrlPathHelper pathHelper) {
        Assert.notNull(pathHelper, "[pathHelper] must not be null");
        this.pathHelper = pathHelper;
    }

    public Collection<Mapping> getMappings() {
        return Collections.unmodifiableCollection(mappings.values());
    }

    protected String appendWildcardsIfMissing(String pattern) {
        if (!pattern.endsWith("/**")) {
            return pattern + "/**";
        }

        return pattern;
    }

    public Mapping registerUiClass(String pattern, Class<? extends UI> uiClass) {
        pattern = appendWildcardsIfMissing(pattern);
        Mapping mapping = new Mapping(pattern, uiClass);
        MapUtils.putOnce(mappings, pattern, mapping);
        return mapping;
    }

    private Mapping findMapping(String path) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        final String normalizedPath = PathUtils.normalize(path);
        Mapping found = mappings.values().stream()
                .sorted(Comparator.comparing(Mapping::getPattern).reversed())
                .filter(it -> pathMatcher.match(it.pattern, normalizedPath))
                .findFirst()
                .orElse(null);

        return found;
    }

    public Mapping findMapping(HttpServletRequest request) {
        String path = getPathHelper().getPathWithinApplication(request);
        return findMapping(path);
    }
}

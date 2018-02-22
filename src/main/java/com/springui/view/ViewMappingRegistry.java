package com.springui.view;

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
public class ViewMappingRegistry {

    public static class Mapping {

        private final String pattern;
        private final Class<? extends View> viewClass;

        public String getPattern() {
            return pattern;
        }

        public Class<? extends View> getViewClass() {
            return viewClass;
        }

        public Mapping(String pattern, Class<? extends View> viewClass) {
            this.pattern = pattern;
            this.viewClass = viewClass;
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(ViewMappingRegistry.class);

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

    public Mapping registerViewClass(String pattern, Class<? extends View> viewClass) {
        Mapping mapping = new Mapping(pattern, viewClass);
        MapUtils.putOnce(mappings, pattern, mapping);
        return mapping;
    }

    public Mapping findMapping(String path) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        final String normalizedPath = PathUtils.normalize(path);
        Mapping found = mappings.values().stream()
                .sorted(Comparator.comparing(Mapping::getPattern).reversed())
                .filter(it -> pathMatcher.match(it.pattern, normalizedPath))
                .findFirst()
                .orElse(null);

        return found;
    }

//    public Mapping findMapping(HttpServletRequest request) {
//        String path = getPathHelper().getPathWithinApplication(request);
//        return findMapping(path);
//    }
}

package com.springui.ui;

import com.springui.event.Action;
import com.springui.i18n.MessageSourceProvider;
import com.springui.util.*;
import com.springui.view.View;
import com.springui.view.ViewMappingRegistry;
import com.springui.view.ViewMappingRegistryFactory;
import com.springui.web.ErrorHandler;
import com.springui.web.UIContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.context.Theme;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Stephan Grundner
 */
public abstract class UI implements ApplicationContextAware, MessageSourceProvider {

    public static class Script {

        String type;
        String src;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public Script(String type, String src) {
            this.type = type;
            this.src = src;
        }

        public Script(String src) {
            this.src = src;
        }

        public Script() { }
    }

    public static final String REDIRECT_URL = UI.class.getName() + "#redirectUrl";

    public static UI forRequest(HttpServletRequest request) {
        UIContext context = UIContext.forSession(request);
        return context.getUi(request);
    }

    public static UI forRequest(WebRequest request) {
        return forRequest(WebRequestUtils.toServletRequest(request));
    }

    @Deprecated
    public static UI forCurrentSession() {
        return forRequest(HttpServletRequestUtils.getCurrentRequest());
    }

    private static final Logger LOG = LoggerFactory.getLogger(UI.class);

    private ApplicationContext applicationContext;
    private UrlPathHelper pathHelper = new UrlPathHelper();

    private Theme theme;

    private Component rootComponent;
    private ErrorHandler errorHandler;

    private ViewMappingRegistry viewMappingRegistry;
    private final Map<String, View> viewByPath = new HashMap<>();

    private final Map<String, Component> components = new HashMap<>();
    private final Deque<Overlay> overlays = new LinkedList<>();

    private final Set<Script> additionalScripts = new LinkedHashSet<>();
    private final Map<String, Object> attributes = new HashMap<>();

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public MessageSource getMessageSource() {
        return getApplicationContext();
    }

    public Theme getTheme() {
        if (theme == null) {
            HttpServletRequest request = HttpServletRequestUtils.getCurrentRequest();
            return RequestContextUtils.getTheme(request);
        }

        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public ViewMappingRegistry getViewMappingRegistry() {
        if (viewMappingRegistry == null) {
            ViewMappingRegistryFactory viewMappingRegistryFactory =
                    applicationContext.getBean(ViewMappingRegistryFactory.class);
            viewMappingRegistry = viewMappingRegistryFactory.createViewMappingRegistry(this);
        }

        return viewMappingRegistry;
    }

    public UIContext getContext() {
        HttpServletRequest request =
                HttpServletRequestUtils.getCurrentRequest();
        return UIContext.forSession(request);
    }

    public String getPath() {
        UIContext context = getContext();
        return context.getPath(this);
    }

    private View getOrCreateView(HttpServletRequest request) {
        String fullPath = pathHelper.getPathWithinApplication(request);
        String uiPath = this.getPath();
        uiPath = PathUtils.normalize(uiPath);
        String viewPath = StringUtils.removeStart(fullPath, uiPath);
        ViewMappingRegistry viewMappingRegistry = getViewMappingRegistry();
        ViewMappingRegistry.Mapping mapping = viewMappingRegistry.findMapping(viewPath);
        if (mapping != null) {
            AntPathMatcher pathMatcher = new AntPathMatcher();
            String path = pathHelper.getPathWithinApplication(request);
            String tail = pathMatcher.extractPathWithinPattern(mapping.getPattern(), path);
            path = StringUtils.removeEnd(path, tail);
            View view = viewByPath.get(path);
            if (view == null) {
                view = BeanFactoryUtils.getPrototypeBean(applicationContext, mapping.getViewClass());
                MapUtils.putOnce(viewByPath, path, view);
            }

            return view;
        }

        return null;
    }

    public void process(HttpServletRequest request) {
        if (request == null) {
            return;
        }

        String path = pathHelper.getPathWithinApplication(request);
        View view = getOrCreateView(request);
        if (view == null) {
            throw new RuntimeException("Unable to create view for path [" + path + "]");
        }
        view.request(new ServletWebRequest(request));
    }

    public String getRedirectUrl(WebRequest request) {
        return (String) request.getAttribute(REDIRECT_URL, WebRequest.SCOPE_REQUEST);
    }

    public String getRedirectUrl(HttpServletRequest request) {
        return getRedirectUrl(HttpServletRequestUtils.toWebRequest(request));
    }

    private static void setRedirectUrl(WebRequest request, String redirectUrl) {
        request.setAttribute(REDIRECT_URL, redirectUrl, WebRequest.SCOPE_REQUEST);
    }

    public void redirectTo(String url) {
        WebRequest request = WebRequestUtils.getCurrentWebRequest();
        setRedirectUrl(request, url);
    }

    public abstract void init(WebRequest request);

    public void performAction(Action action) {
        String componentId = action.getComponentId();
        Component component = getComponent(componentId);
        component.performAction(action);
    }

    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    public Map<String, Component> getComponents() {
        return Collections.unmodifiableMap(components);
    }

    public Component getComponent(String componentId) {
        return components.get(componentId);
    }

    public Component getRootComponent() {
        return rootComponent;
    }

    public void setRootComponent(Component rootComponent) {
        this.rootComponent = rootComponent;
        if (rootComponent != null) {
            rootComponent.walk(this::attach);
        }
    }

    protected boolean attach(Component component) {
        if (components.put(component.getId(), component) == null) {
            component.setUi(this);
            return true;
        }

        return false;
    }

    protected boolean detach(Component component) {
        if (components.remove(component.getId(), component)) {
//            component.setUi(null);
            return true;
        }

        return false;
    }

    public Collection<Overlay> getOverlays() {
        return Collections.unmodifiableCollection(overlays);
    }

    public void displayOverlay(Overlay overlay) {
        overlays.remove(overlay);
        overlays.addFirst(overlay);

        overlay.walk(this::attach);
    }

    public void hideOverlay(Overlay overlay) {
        overlays.remove(overlay);
        detach(overlay);
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public Set<Script> getAdditionalScripts() {
        return Collections.unmodifiableSet(additionalScripts);
    }

    public boolean addScript(Script script) {
        return additionalScripts.add(script);
    }

    public boolean removeScript(Script script) {
        return additionalScripts.remove(script);
    }

    public Object getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    public void setAttribute(String attributeName, Object attributeValue) {
        attributes.put(attributeName, attributeValue);
    }
}

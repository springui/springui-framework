package com.springui.ui;

import com.springui.i18n.MessageSourceProvider;
import com.springui.util.PathUtils;
import com.springui.util.WebRequestUtils;
import com.springui.web.ViewMappingRegistry;
import com.springui.web.ViewNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.context.Theme;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/ui")
public class UI extends SingleComponentContainer<Component> implements ApplicationContextAware, MessageSourceProvider {

    public static UI forRequest(WebRequest request) {
        return (UI) request.getAttribute(UI.class.getName(), WebRequest.SCOPE_SESSION);
    }

    public static UI forCurrentSession() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return (UI) requestAttributes.getAttribute(UI.class.getName(), RequestAttributes.SCOPE_SESSION);
    }

    private static final Logger LOG = LoggerFactory.getLogger(UI.class);

    private ApplicationContext applicationContext;

    private Theme theme;

    private ViewMappingRegistry viewMappingRegistry;
    private final Map<String, View> viewByPath = new HashMap<>();
    private View activeView;

    private final Map<String, Component> components = new HashMap<>();

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
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public ViewMappingRegistry getViewMappingRegistry() {
        if (viewMappingRegistry == null) {
            viewMappingRegistry = applicationContext.getBean(ViewMappingRegistry.class);
        }

        return viewMappingRegistry;
    }

    public String getActionPath() {
        return "action";
    }

    public String getUploadPath() {
        return "upload";
    }

    @Deprecated
    public void registerViewClass(String path, Class<? extends View> viewClass) {
        ViewMappingRegistry viewMappingRegistry = getViewMappingRegistry();
        viewMappingRegistry.register(path, viewClass);
    }

    private View getOrCreateView(String path) {
        ViewMappingRegistry viewMappingRegistry = getViewMappingRegistry();
        Class<? extends View> viewClass = viewMappingRegistry.lookup(path);
        if (viewClass != null) {
            View view = viewByPath.get(path);
            if (view == null) {
                view = applicationContext.getBean(viewClass);
                view.setPath(path);
                viewByPath.put(path, view);
            }

            return view;
        }

        throw new ViewNotFoundException(path);
    }

    public void activate(WebRequest request) {
        String path = WebRequestUtils.getPath(request);
        path = PathUtils.normalize(path);
        View view = getOrCreateView(path);

        view.activated(request);

        activeView = view;
    }

    public final View getActiveView() {
        return activeView;
    }

    public void init(WebRequest request) { }

    @Override
    public final void setParent(Component parent) {
        throw new RuntimeException("UI must not have a parent");
    }

    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    public void bindTo(WebRequest request) {
        request.setAttribute(UI.class.getName(), this, WebRequest.SCOPE_SESSION);
    }

    public Map<String, Component> getComponents() {
        return Collections.unmodifiableMap(components);
    }

    public Component getComponent(String componentId) {
        return components.get(componentId);
    }

    @Override
    public void setComponent(Component component) {
        super.setComponent(component);
    }

    protected boolean attach(Component component) {
        if (components.put(component.getId(), component) == null) {
//            component.setUi(this);
            component.attached();
            return true;
        }

        return false;
    }

    protected boolean detach(Component component) {
        if (components.remove(component.getId(), component)) {
            component.detached();
            return true;
        }

        return false;
    }
}

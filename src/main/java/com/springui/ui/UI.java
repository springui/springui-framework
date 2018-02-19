package com.springui.ui;

import com.springui.i18n.MessageSourceProvider;
import com.springui.util.BeanFactoryUtils;
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

    public static final String ATTRIBUTE_NAME = "ui";
    public static final String REDIRECT_URL = UI.class.getName() + "#redirectUrl";

    public static UI forRequest(WebRequest request) {
        return (UI) request.getAttribute(ATTRIBUTE_NAME, WebRequest.SCOPE_SESSION);
    }

    public static UI forCurrentSession() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return (UI) requestAttributes.getAttribute(ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION);
    }

    private static final Logger LOG = LoggerFactory.getLogger(UI.class);

    private ApplicationContext applicationContext;

    private Theme theme;

    private ViewMappingRegistry viewMappingRegistry;
    private final Map<String, View> viewByPath = new HashMap<>();

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
        path = PathUtils.normalize(path);
        Class<? extends View> viewClass = viewMappingRegistry.findViewClass(path);

        if (viewClass != null) {
            View view = viewByPath.get(path);
            if (view == null) {
                view = BeanFactoryUtils.getPrototypeBean(applicationContext, viewClass);
                viewByPath.put(path, view);
            }

            return view;
        }

        throw new ViewNotFoundException(path);
    }

    protected Component getViewComponent() {
        throw new UnsupportedOperationException();
    }

    protected void setViewComponent(Component component) {
        setComponent(component);
    }

    public void process(WebRequest request) {
        if (request == null) {
            setViewComponent(null);
            return;
        }

        String path = WebRequestUtils.getPath(request);
        path = PathUtils.normalize(path);
        View view = getOrCreateView(path);
        if (view != null) {

            view.refresh(request);

            setViewComponent(view.getComponent());
        }
    }

    public String getRedirectUrl(WebRequest request) {
        return (String) request.getAttribute(REDIRECT_URL, WebRequest.SCOPE_REQUEST);
    }

    public void setRedirectUrl(WebRequest request, String redirectUrl) {
        request.setAttribute(REDIRECT_URL, redirectUrl, WebRequest.SCOPE_REQUEST);
    }

    public void redirectTo(String url) {
        WebRequest request = WebRequestUtils.getCurrentWebRequest();
        setRedirectUrl(request, url);
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
        request.setAttribute(ATTRIBUTE_NAME, this, WebRequest.SCOPE_SESSION);
    }

    public Map<String, Component> getComponents() {
        return Collections.unmodifiableMap(components);
    }

    public Component getComponent(String componentId) {
        return components.get(componentId);
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

package com.springui.ui;

import com.springui.i18n.MessageSourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ThemeResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/ui")
public abstract class UI extends SingleComponentContainer<Component> implements ApplicationContextAware, MessageSourceProvider {

    public static final String SESSION_ATTRIBUTE_NAME = "ui";

    public static UI getCurrent() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return (UI) requestAttributes.getAttribute(UI.class.getName(), RequestAttributes.SCOPE_REQUEST);
    }

    private static final Logger LOG = LoggerFactory.getLogger(UI.class);

    private ApplicationContext applicationContext;
    private String path;
    private Theme theme;

    @Deprecated
    private ViewRegistry viewRegistry;
    private View activeView;

    private final Map<String, Component> components = new HashMap<>();

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Theme getTheme() {
        return theme;
    }

    private void setTheme(Theme theme) {
        this.theme = theme;
    }

    public String getActionPath() {
//        return UriComponentsBuilder
//                .fromPath(getPath())
//                .pathSegment("action")
//                .toUriString();
        return "/ui/action";
    }

    public String getUploadPath() {
//        return UriComponentsBuilder
//                .fromPath(getPath())
//                .pathSegment("upload")
//                .toUriString();
        return "/ui/upload";
    }

    @Deprecated
    private ViewRegistry getViewRegistry() {
        if (viewRegistry == null) {
            viewRegistry = new ViewRegistry();
        }

        return viewRegistry;
    }

    public void registerViewClass(String path, Class<? extends View> viewClass) {
        getViewRegistry().registerView(path, viewClass);
    }

    public View navigate(WebRequest request) {
        View view = getViewRegistry().navigate(request);
        activeView = view;
        return view;
    }

    public final View getActiveView() {
        return activeView;
    }

    public Map<String, Component> getComponents() {
        return Collections.unmodifiableMap(components);
    }

    public Component getComponent(String componentId) {
        return components.get(componentId);
    }

    protected boolean attach(Component component) {
        if (components.put(component.getId(), component) == null) {
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

    @Override
    public final void setParent(Component parent) {
        throw new RuntimeException("UI must not have a parent");
    }

    public MessageSource getMessageSource() {
        return applicationContext;
    }

    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    public void init(HttpServletRequest request) {
        request.setAttribute(UI.class.getName(), this);

        ThemeResolver themeResolver = applicationContext.getBean(ThemeResolver.class);
        String themeName = themeResolver.resolveThemeName(request);
        ThemeSource themeSource = applicationContext.getBean(ThemeSource.class);
        Theme theme = themeSource.getTheme(themeName);
        setTheme(theme);
    }

//    public UI(ApplicationContext applicationContext, String path) {
//        this.applicationContext = applicationContext;
//        this.path = path;
//    }
}

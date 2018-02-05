package com.springui.ui.component;

import com.springui.i18n.MessageSourceProvider;
import com.springui.web.PathUtils;
import com.springui.web.UITheme;
import com.springui.web.UIThemeSource;
import com.springui.web.WebRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
public abstract class UI extends SingleComponentContainer<Component> implements MessageSourceProvider {

    public static UI getCurrent() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return (UI) requestAttributes.getAttribute(UI.class.getName(), RequestAttributes.SCOPE_REQUEST);
    }

    private static final Logger LOG = LoggerFactory.getLogger(UI.class);

    {
        setTemplate("ui/ui");
    }

    private ApplicationContext applicationContext;

    private String path;

    @Deprecated
    private ViewRegistry viewRegistry;
    private View activeView;

    private UIThemeSource themeSource;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private final Map<String, Component> components = new HashMap<>();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = PathUtils.normalize(path);
    }

    public String getActionPath() {
        return UriComponentsBuilder
                .fromPath(getPath())
                .pathSegment("action")
                .toUriString();
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

    private UIThemeSource getThemeSource() {
        if (themeSource == null) {
            themeSource = applicationContext.getBean(UIThemeSource.class);
        }

        return themeSource;
    }

    public UITheme getTheme() {
        UIThemeSource themeSource = getThemeSource();
        ThemeResolver themeResolver = applicationContext.getBean(ThemeResolver.class);
        String themeName = themeResolver.resolveThemeName(WebRequestUtils.getCurrentServletRequest());
        return themeSource.getTheme(themeName);
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
        ThemeResolver themeResolver = applicationContext.getBean(ThemeResolver.class);
        String themeName = themeResolver.resolveThemeName(request);
    }

    public UI(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}

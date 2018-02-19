package com.springui.ui;

import com.springui.i18n.MessageSourceProvider;
import com.springui.util.BeanFactoryUtils;
import com.springui.util.HttpServletRequestUtils;
import com.springui.util.PathUtils;
import com.springui.util.WebRequestUtils;
import com.springui.web.View;
import com.springui.web.ViewMappingRegistry;
import com.springui.web.ViewNotFoundException;
import org.apache.commons.lang3.StringUtils;
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
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/ui")
public class UI extends SingleComponentContainer<Component> implements ApplicationContextAware, MessageSourceProvider {

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

    public static final String ATTRIBUTE_NAME = "ui";
    public static final String REDIRECT_URL = UI.class.getName() + "#redirectUrl";

    public static UI forRequest(WebRequest request) {
        return (UI) request.getAttribute(ATTRIBUTE_NAME, WebRequest.SCOPE_SESSION);
    }

    public static UI forRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (UI) session.getAttribute(ATTRIBUTE_NAME);
        }

        return null;
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

    private final Set<Script> additionalScripts = new LinkedHashSet<>();

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
            ThemeSource themeSource = applicationContext.getBean(ThemeSource.class);
            ThemeResolver themeResolver = applicationContext.getBean(ThemeResolver.class);
            HttpServletRequest request = HttpServletRequestUtils.getCurrentRequest();
            String themeName = themeResolver.resolveThemeName(request);
            return themeSource.getTheme(themeName);
        }

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

    @Deprecated
    public void registerViewClass(String path, Class<? extends View> viewClass) {
        ViewMappingRegistry viewMappingRegistry = getViewMappingRegistry();
        viewMappingRegistry.register(path, viewClass);
    }

    public View getView(String path) {
        return viewByPath.get(path);
    }

    private View getOrCreateView(String path) {
        ViewMappingRegistry viewMappingRegistry = getViewMappingRegistry();
        path = PathUtils.normalize(path);
        Class<? extends View> viewClass = viewMappingRegistry.findViewClass(path);

        if (viewClass != null) {
            View view = getView(path);
            if (view == null) {
                view = BeanFactoryUtils.getPrototypeBean(applicationContext, viewClass);
                viewByPath.put(path, view);
            }

            return view;
        }

        throw new ViewNotFoundException(path);
    }

    public void process(WebRequest request) {
        if (request == null) {
            return;
        }

        String path = WebRequestUtils.getPath(request);
        path = PathUtils.normalize(path);
        View view = getOrCreateView(path);
        if (view != null) {
            view.request(request);
        }
    }

    public String getPath(Class<? extends View> viewClass) {
        return getViewMappingRegistry().findPath(viewClass);
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

    private void bindTo(WebRequest request) {
        request.setAttribute(ATTRIBUTE_NAME, this, WebRequest.SCOPE_SESSION);
    }

    public void init(WebRequest request) {
        bindTo(request);
    }

    @Override
    public final void setParent(Component parent) {
        throw new RuntimeException("UI must not have a parent");
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
        return super.getComponent();
    }

    public void setRootComponent(Component component) {
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

    public Set<Script> getAdditionalScripts() {
        return Collections.unmodifiableSet(additionalScripts);
    }

    public boolean addScript(Script script) {
        return additionalScripts.add(script);
    }

    public boolean removeScript(Script script) {
        return additionalScripts.remove(script);
    }
}

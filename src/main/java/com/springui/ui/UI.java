package com.springui.ui;

import com.springui.collection.MapUtils;
import com.springui.i18n.MessageSourceProvider;
import com.springui.web.PathMappings;
import com.springui.util.PathUtils;
import com.springui.web.ViewMapping;
import com.springui.util.WebRequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
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
public abstract class UI extends SingleComponentContainer<Component> implements MessageSourceProvider {

    @Deprecated
    public static UI getCurrent() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return (UI) requestAttributes.getAttribute(UI.class.getName(), RequestAttributes.SCOPE_REQUEST);
    }

    private static final Logger LOG = LoggerFactory.getLogger(UI.class);

    private UIContext context;
    private String path;
    private Theme theme;

    private PathMappings<ViewMapping> viewMappings = new PathMappings<>();
    private View activeView;

    private final Map<String, Component> components = new HashMap<>();

    @Deprecated
    public ApplicationContext getApplicationContext() {
        return context.getApplicationContext();
    }

    @Deprecated
    public MessageSource getMessageSource() {
        return getApplicationContext();
    }

    public UIContext getContext() {
        return context;
    }

    protected void setContext(UIContext context) {
        this.context = context;
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
        return "action";
    }

    public String getUploadPath() {
        return "upload";
    }

    public void registerViewClass(String path, Class<? extends View> viewClass) {
        Assert.hasLength(path, "[path] must not be empty");
        Assert.notNull(viewClass, "[viewClass] must not be null");
        MapUtils.putValueOnce(viewMappings, path, new ViewMapping(path, viewClass));
    }

    private View getView(String path, boolean create) {
        ViewMapping mapping = viewMappings.find(path);
        if (mapping != null) {
            View view = mapping.getView();
            if (view == null && create) {
                view = BeanUtils.instantiate(mapping.getViewClass());
                view.setPath(path);
                mapping.setView(view);
            }
            return view;
        } else {
            throw new RuntimeException(String.format("No view registered for path [%s]", path));
        }
    }

    public View activate(WebRequest request) {
        UI ui = this;
        String path = WebRequestUtils.getPath(request);
        String prefix = ui.getPath();
        String postfix = StringUtils.removeStart(path, prefix);

        path = PathUtils.normalize(postfix);

        View view = getView(path, true);

        MultiValueMap<String, String> params =
                WebRequestUtils.getQueryParams(request);

        view.activate(ui, params);

        activeView = view;

        return view;
    }

    public final View getActiveView() {
        return activeView;
    }

    @Override
    public final void setParent(Component parent) {
        throw new RuntimeException("UI must not have a parent");
    }

    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    public void init(HttpServletRequest request) {
        request.setAttribute(UI.class.getName(), this);
        ThemeResolver themeResolver = getApplicationContext().getBean(ThemeResolver.class);
        String themeName = themeResolver.resolveThemeName(request);
        ThemeSource themeSource = getApplicationContext().getBean(ThemeSource.class);
        Theme theme = themeSource.getTheme(themeName);
        setTheme(theme);
    }

    public void bindTo(WebRequest request) {
        request.setAttribute(UI.class.getName(), this, WebRequest.SCOPE_REQUEST);
    }

    public Map<String, Component> getComponents() {
        return Collections.unmodifiableMap(components);
    }

    public Component getComponent(String componentId) {
        return components.get(componentId);
    }

    protected boolean attach(Component component) {
        if (components.put(component.getId(), component) == null) {
            component.setUi(this);
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

    public UI(String path) {
        this.path = path;
    }

    public UI() { }
}

package com.springui.ui;

import com.springui.i18n.MessageSourceProvider;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/ui")
public abstract class UI extends SingleComponentContainer<Component> implements MessageSourceProvider {

    public static final String SESSION_ATTRIBUTE_NAME = "ui";

    @Deprecated
    public static UI getCurrent() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return (UI) requestAttributes.getAttribute(UI.class.getName(), RequestAttributes.SCOPE_REQUEST);
    }

    private static final Logger LOG = LoggerFactory.getLogger(UI.class);

    private UIContext context;
    private String path;


    private Theme theme;

    @Deprecated
    private ViewRegistry viewRegistry;
    private View activeView;


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

    public void setCurrent() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        requestAttributes.setAttribute(UI.class.getName(), this, RequestAttributes.SCOPE_REQUEST);
    }

    public UI(String path) {
        this.path = path;
    }

    public UI() { }
}

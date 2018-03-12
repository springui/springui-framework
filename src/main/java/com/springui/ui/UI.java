package com.springui.ui;

import com.springui.i18n.Message;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui")
public class UI extends AbstractSingleComponentContainer {

    private static final String SESSION_ATTRIBUTE_NAME = UI.class.getName();

    public static UI forSession(HttpSession session, Supplier<UI> factory) {
        UI ui = (UI) session.getAttribute(SESSION_ATTRIBUTE_NAME);
        if (ui == null) {
            ui = factory.get();
            session.setAttribute(SESSION_ATTRIBUTE_NAME, ui);
        }

        return ui;
    }

    public static UI forSession(HttpSession session) {
        return forSession(session, UI::new);
    }

    private Map<String, Component> components = new HashMap<>();
    private Deque<Overlay> overlays = new LinkedList<>();

    public final Message getTitle() {
        return getCaption();
    }

    public final void setTitle(Message title) {
        setCaption(title);
    }

    public Map<String, Component> getComponents() {
        return Collections.unmodifiableMap(components);
    }

    private void registerComponent(Component component) {
        if (components.put(component.getId(), component) == null) {
            component.setUi(this);
        }
    }

    private void deregisterComponent(Component component) {
        if (components.remove(component.getId(), component)) {
            component.setUi(null);
        }
    }

    void attach(Component component) {
        component.walk(this::registerComponent);
    }

    void detach(Component component) {
        component.walk(this::deregisterComponent);
    }

    public Collection<Overlay> getOverlays() {
        return Collections.unmodifiableCollection(overlays);
    }

    public boolean addOverlay(Overlay overlay) {
        boolean removed = removeOverlay(overlay);
        if (overlays.offerFirst(overlay)) {
            if (!removed) {
                attach(overlay);
            }

            return true;
        }

        return false;
    }

    public boolean removeOverlay(Overlay overlay) {
        if (overlays.remove(overlay)) {
            detach(overlay);
            return true;
        }

        return false;
    }
}

package com.springui.ui;

import java.util.Set;

/**
 * @author Stephan Grundner
 */
public interface ComponentsContainer extends Component {

    @Deprecated
    static void notifyAttached(Component component, AttachEvent event) {
        if (component instanceof ComponentsContainer) {
            for (AttachListener attachListener : ((ComponentsContainer) component).getAttachListeners()) {
                attachListener.attached(event);
            }

            Component parent = component.getParent();
            if (parent != null && parent != component) {
                notifyAttached(parent, event);
            }
        }
    }

    static void notifyAttached(AttachListener attachListener, AttachEvent event) {
        if (attachListener != null) {
            attachListener.attached(event);
        }

        Component component = event.getComponent();
        Component parent = component.getParent();
        if (parent != null) {
            notifyAttached(parent, event);
        }
    }

    static void notifyAttached(AttachEvent event) {
        Component component = event.getComponent();
        Component parent = component.getParent();
        if (parent instanceof ComponentsContainer) {
            for (AttachListener attachListener : ((ComponentsContainer) parent).getAttachListeners()) {
                notifyAttached(attachListener, event);
            }

            notifyAttached(parent, event);
        }
    }

    static void notifyAttached(Component component) {
        notifyAttached(new AttachEvent(component));
    }

    class AttachEvent {

        private final Component component;

        public Component getComponent() {
            return component;
        }

        public AttachEvent(Component component) {
            this.component = component;
        }
    }

    interface AttachListener {

        void attached(AttachEvent event);
    }

    static void notifyDetached(ComponentsContainer container, DetachEvent event) {
        for (DetachListener detachListener : container.getDetachListeners()) {
            detachListener.detached(event);
        }

        Component parent = container.getParent();
        if (parent instanceof ComponentsContainer) {
            notifyDetached((ComponentsContainer) parent, event);
        }
    }

    static void notifyDetached(Component component, ComponentsContainer container) {
        notifyDetached(container, new DetachEvent(component, container));
    }

    class DetachEvent {

        private final Component component;
        private final ComponentsContainer container;

        public Component getComponent() {
            return component;
        }

        public ComponentsContainer getContainer() {
            return container;
        }

        public DetachEvent(Component component, ComponentsContainer container) {
            this.component = component;
            this.container = container;
        }
    }

    interface DetachListener {

        void detached(DetachEvent event);
    }

    Set<AttachListener> getAttachListeners();
    void addAttachListener(AttachListener attachListener);
    void removeAttachListener(AttachListener attachListener);

    Set<DetachListener> getDetachListeners();
    void addDetachListener(DetachListener detachListener);
    void removeDetachListener(DetachListener detachListener);

    void addComponent(Component component);
    void removeComponent(Component component);
}

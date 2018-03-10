package com.springui.ui;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractComponentsContainer extends AbstractComponent implements ComponentsContainer {

    private final Set<AttachListener> attachListeners = new LinkedHashSet<>();
    private final Set<DetachListener> detachListeners = new LinkedHashSet<>();

    @Override
    public Set<AttachListener> getAttachListeners() {
        return Collections.unmodifiableSet(attachListeners);
    }

    @Override
    public void addAttachListener(AttachListener attachListener) {
        attachListeners.add(attachListener);
    }

    @Override
    public void removeAttachListener(AttachListener attachListener) {
        attachListeners.remove(attachListener);
    }

    @Override
    public Set<DetachListener> getDetachListeners() {
        return Collections.unmodifiableSet(detachListeners);
    }

    @Override
    public void addDetachListener(DetachListener detachListener) {
        detachListeners.add(detachListener);
    }

    @Override
    public void removeDetachListener(DetachListener detachListener) {
        detachListeners.remove(detachListener);
    }
}

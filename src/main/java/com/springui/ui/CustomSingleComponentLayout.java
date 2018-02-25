package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public class CustomSingleComponentLayout extends SingleComponentLayout {

    private String componentName;

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public final ComponentsMapContainer getLayout() {
        return (ComponentsMapContainer) super.getComponent();
    }

    public final void setLayout(ComponentsMapContainer layout, String componentName) {
        super.setComponent(layout);
        this.componentName = componentName;
    }

    @Override
    public final Component getComponent() {
        return getLayout().getComponent(getComponentName());
    }

    @Override
    public final void setComponent(Component component) {
        getLayout().addComponent(getComponentName(), component);
    }
}

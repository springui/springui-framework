package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public class CustomSingleComponentLayout extends SingleComponentContainer {

    private String componentName;

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public final CustomLayout getLayout() {
        return (CustomLayout) super.getComponent();
    }

    public final void setLayout(CustomLayout layout, String componentName) {
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

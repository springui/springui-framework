package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public class CustomSingleComponentLayout extends SingleComponentContainer {

    public final CustomLayout getLayout() {
        return (CustomLayout) super.getComponent();
    }

//    private void replace(SingleComponentContainer oldOne, SingleComponentContainer newOne) {
//        if (oldOne != null && newOne != null) {
//            newOne.setComponent(oldOne.getComponent());
//        }
//    }

    public final void setLayout(CustomLayout layout) {
//        replace(getDecoratingContainer(), decoratingContainer);
        super.setComponent(layout);
    }

    @Override
    public final Component getComponent() {
        return getLayout().getComponent("main");
    }

    @Override
    public final void setComponent(Component component) {
        getLayout().addComponent("main", component);
    }
}

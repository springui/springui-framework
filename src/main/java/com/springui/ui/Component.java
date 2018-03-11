package com.springui.ui;

import com.springui.web.UIRequest;

import java.util.Set;

/**
 * @author Stephan Grundner
 */
public interface Component {

    static void performAction(Action action) {
        Component component = action.getComponent();
        for (ActionListener actionListener : component.getActionListeners()) {
            actionListener.perform(action);
        }
    }

    class Action {

        private final UIRequest request;
        private final Component component;
        private final String[] events;

        public UIRequest getRequest() {
            return request;
        }

        public Component getComponent() {
            return component;
        }

        public String[] getEvents() {
            return events;
        }

        public Action(UIRequest request, Component component, String[] events) {
            this.request = request;
            this.component = component;
            this.events = events;
        }
    }

    interface ActionListener {

        void perform(Action action);
    }

    enum Color {
        PRIMARY,
        SECONDARY,
        SUCCESS,
        DANGER
    }

    enum Size {
        SMALL,
        NORMAL,
        LARGE
    }

    String getId();

    UI getUi();
    void setUi(UI ui);

    Component getParent();
    void setParent(Component parent);

    Set<ActionListener> getActionListeners();
    boolean addActionListener(ActionListener actionListener);
    boolean removeActionListener(ActionListener actionListener);

    Color getColor();
    void setColor(Color color);

    Size getSize();
    void setSize(Size size);

    void walk(ComponentVisitor visitor);
}

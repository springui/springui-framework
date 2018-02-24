package com.springui.web;

import com.springui.ui.*;
import com.springui.util.MapBuilder;

import java.util.Map;

/**
 * @author Stephan Grundner
 */
public class BootstrapTemplateResolver implements TemplateResolver {

    private static final Map<Class<? extends Component>, String> mapping =
            MapBuilder.<Class<? extends Component>, String>createHashMap()
                    .put(SingleComponentContainer.class, "bootstrap/ui/single-component-container")
                    .put(Text.class, "bootstrap/ui/text")
                    .put(TextField.class, "bootstrap/ui/text-field")
                    .put(MultilineTextField.class, "bootstrap/ui/multiline-text-field")
                    .put(RichTextField.class, "bootstrap/ui/rich-text-field")
                    .put(BooleanField.class, "bootstrap/ui/boolean-field")
                    .put(DateField.class, "bootstrap/ui/date-field")
                    .put(FormLayout.class, "bootstrap/ui/form-layout::components")
                    .put(SingleSelect.class, "bootstrap/ui/single-select-default")
                    .put(MultiSelect.class, "bootstrap/ui/multi-select-default")
                    .put(Overlay.class, "bootstrap/ui/modal")
                    .put(Pagination.class, "bootstrap/ui/pagination")
                    .put(Table.class, "bootstrap/ui/table")
                    .put(Table.Row.class, "bootstrap/ui/table-row")
                    .put(Trigger.class, "bootstrap/ui/button")
                    .put(Upload.class, "bootstrap/ui/upload")
                    .getUnmodifiableMap();

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean accept(String theme) {
        return "bootstrap".equals(theme);
    }

    @Override
    public String resolveTemplate(String theme, UI ui) {
        return "bootstrap/ui/ui";
    }

//    protected String resolveTemplate(FormLayout.ComponentContainer container) {
//        Component component = container.getComponent();
//        if (component instanceof BooleanField) {
//            return "bootstrap/ui/form-layout::boolean";
//        }
//
//        return "bootstrap/ui/form-layout::default";
//    }

    @SuppressWarnings("unchecked")
    private <T extends Component> String resolveTemplate(Class<T> componentClass) {
        String template = mapping.get(componentClass);
        if (template == null) {
            Class<?> superClass = componentClass.getSuperclass();
            if (Component.class.isAssignableFrom(superClass)) {
                return resolveTemplate((Class<T>) superClass);
            }
        }

        return template;
    }

    @Override
    public String resolveTemplate(String theme, Component component) {
        String template = resolveTemplate(component.getClass());

        return template;
    }
}

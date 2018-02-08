package com.springui.ui;

import com.springui.collection.MapUtils;

import java.util.*;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/form")
public class Form extends ComponentsContainer<Form.FieldContainer> {

//    @Template("{theme}/ui/field-container")
    public static class FieldContainer extends Component {

        private Field field;

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;

            field.setParent(this);
        }

        @Override
        public void walk(ComponentVisitor visitor) {
            super.walk(visitor);
            if (field != null) {
                field.walk(visitor);
            }
        }
    }

    public static class FieldContainerFactory {

        public FieldContainer createFieldContainer(Field field) {
            FieldContainer fieldContainer = new FieldContainer();
            fieldContainer.setField(field);

            if (field instanceof BooleanField) {
                fieldContainer.setTemplate("{theme}/ui/field-container::boolean");
            } else {
                fieldContainer.setTemplate("{theme}/ui/field-container::default");
            }

            return fieldContainer;
        }
    }

    private final Map<Field, FieldContainer> fields = new IdentityHashMap<>();
    private FieldContainerFactory fieldContainerFactory;

    public FieldContainerFactory getFieldContainerFactory() {
        if (fieldContainerFactory == null) {
            fieldContainerFactory = new FieldContainerFactory();
        }

        return fieldContainerFactory;
    }

    public void setFieldContainerFactory(FieldContainerFactory fieldContainerFactory) {
        this.fieldContainerFactory = fieldContainerFactory;
    }

    public boolean addField(Field field) {
        if (!fields.containsKey(field)) {
            FieldContainerFactory fieldContainerFactory = getFieldContainerFactory();
            FieldContainer fieldContainer =
                    fieldContainerFactory.createFieldContainer(field);
            fieldContainer.setParent(this);
            MapUtils.putValueOnce(fields, field, fieldContainer);

            return true;
        }

        return false;
    }

    private Collection<FieldContainer> getFieldContainers() {
        return Collections.unmodifiableCollection(fields.values());
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        super.walk(visitor);
        for (FieldContainer fieldContainer : getFieldContainers()) {
            fieldContainer.walk(visitor);
        }
    }

    @Override
    public Iterator<FieldContainer> iterator() {
        return getFieldContainers().iterator();
    }
}

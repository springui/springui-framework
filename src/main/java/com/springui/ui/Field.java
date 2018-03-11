package com.springui.ui;

/**
 * @author Stephan Grundner
 */
public interface Field<T> extends Component {

    class ValueChange<T> {

        private Field<T> field;
        private T previousValue;

        public Field<T> getField() {
            return field;
        }

        public T getPreviousValue() {
            return previousValue;
        }

        public ValueChange(Field<T> field, T previousValue) {
            this.field = field;
            this.previousValue = previousValue;
        }
    }

    interface ValueChangeListener<T> {

        void valueChanged(ValueChange<T> change);
    }

    T getValue();
    void setValue(T value);
}

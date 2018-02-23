package com.springui.ui;

import java.util.Date;

/**
 * @author Stephan Grundner
 */
public class DateField extends Field<Date> {

    private Date value;

    @Override
    public Date getValue() {
        return value;
    }

    @Override
    protected void valueChanged(Date newValue, Date oldValue) {
        value = newValue;
    }
}

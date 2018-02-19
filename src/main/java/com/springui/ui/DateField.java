package com.springui.ui;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/date-field")
public class DateField extends Field<Date> {

    @DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
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

package com.springui.ui.component;

import com.springui.ui.Template;

import java.util.Date;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/date-field")
public class DateField extends Field<Date> {

    private Date value;
    private String dateFormat;

    @Override
    public Date getValue() {
        return value;
    }

    @Override
    protected void valueChanged(Date newValue, Date oldValue) {
        value = newValue;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}

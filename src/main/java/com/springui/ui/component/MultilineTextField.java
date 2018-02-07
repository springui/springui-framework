package com.springui.ui.component;

import com.springui.ui.Template;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/multiline-text-field")
public class MultilineTextField extends TextField {

    private int columns;
    private int lines;

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }
}

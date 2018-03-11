package com.springui.ui.field;

import com.springui.ui.Template;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/multiline-text-field")
public class MultilineTextField extends TextField {

    private int lines;

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }
}

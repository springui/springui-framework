package com.springui.ui;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/ui/pagination")
public class Pagination extends Component {

    private int pages;
    private int index;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

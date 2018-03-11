package com.springui.ui.menu;

import com.springui.i18n.Message;
import com.springui.ui.AbstractComponent;
import com.springui.ui.ComponentVisitor;
import com.springui.ui.Template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/menu")
public class Menu extends AbstractComponent {

    public static class Item extends AbstractComponent {

        private final boolean separator;

        public boolean isSeparator() {
            return separator;
        }

        @Override
        public void walk(ComponentVisitor visitor) {
            visitor.visit(this);
        }

        public Item(boolean separator) {
            this.separator = separator;
        }

        public Item(Message caption) {
            this(false);
            setCaption(caption);
        }

        public Item() {
            this(false);
        }
    }

    private final List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean addItem(Item item) {
        if (items.add(item)) {
            item.setParent(this);
            return true;
        }

        return false;
    }

    public boolean removeItem(Item item) {
        if (items.remove(item)) {
            item.setParent(null);
            return true;
        }

        return false;
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        items.forEach(visitor::visit);
        visitor.visit(this);
    }
}

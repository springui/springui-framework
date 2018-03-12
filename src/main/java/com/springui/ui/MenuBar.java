package com.springui.ui;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
@Template("{theme}/menu-bar")
public class MenuBar extends AbstractComponent {

    private final Set<Menu> menus = new LinkedHashSet<>();

    public Set<Menu> getMenus() {
        return Collections.unmodifiableSet(menus);
    }

    public void addMenu(Menu menu) {
        if (menus.add(menu)) {
            menu.setParent(this);
        }
    }

    @Override
    public void walk(ComponentVisitor visitor) {
//        menus.forEach(visitor::visit);
//        TODO Warum funktioniert obriges nicht???
        menus.forEach(menu -> {
            menu.walk(visitor);
        });
        visitor.visit(this);
    }
}

package com.springui.ui;

import org.springframework.util.Assert;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
public class FlowLayout extends AbstractComponentsContainer implements Layout {

    public enum Direction {
        VERTICAL,
        HORIZONTAL
    }

    private Direction direction = Direction.VERTICAL;
    private final Set<Component> components = new LinkedHashSet<>();

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        Assert.notNull(direction,"[direction] must not be null");
        Assert.isTrue(direction == Direction.VERTICAL,"[direction] must not be set to HORIZONTAL");
        this.direction = direction;
    }

    @Override
    public void addComponent(Component component) {
        if (components.add(component)) {
            component.setParent(this);
        }
    }

    @Override
    public void removeComponent(Component component) {
        if (components.remove(component)) {
            component.setParent(null);
        }
    }

    @Override
    public void walk(ComponentVisitor visitor) {
        visitor.visit(this);
        components.forEach(visitor::visit);
    }
}

package com.springui.web;

import com.springui.ui.UI;

/**
 * @author Stephan Grundner
 */
public class AbstractMappingRegistry<T, M extends AbstractMappingRegistry.AbstractMapping<T>> {

    public static abstract class AbstractMapping<T> {

        private String pattern;
        private Class<? extends T> mappedClass;

        public AbstractMapping(String pattern, Class<? extends T> mappedClass) {
            this.pattern = pattern;
            this.mappedClass = mappedClass;
        }
    }

    protected M register(String pattern, Class<? extends T> tClass) {

        return null;
    }
}

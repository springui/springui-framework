package com.springui.thymeleaf.processor;

import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author Stephan Grundner
 */
public class ComponentReplaceProcessor extends AbstractComponentProcessor {

    private static final String ELEMENT_NAME = "replace";

    public ComponentReplaceProcessor(String dialectPrefix, int precedence) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ELEMENT_NAME, true, precedence, false);
    }
}

package com.springui.thymeleaf;

import com.springui.thymeleaf.processor.ComponentIncludeProcessor;
import com.springui.thymeleaf.processor.ComponentReplaceProcessor;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
public class ComponentDialect extends AbstractProcessorDialect {

    private static final String PREFIX = "ui";

//    private Set<TemplateResolver> templateResolvers;
//
//    public Set<TemplateResolver> getTemplateResolvers() {
//        return templateResolvers;
//    }
//
//    public void setTemplateResolvers(Set<TemplateResolver> templateResolvers) {
//        this.templateResolvers = templateResolvers;
//    }

    public ComponentReplaceProcessor createReplaceProcessor() {
        return new ComponentReplaceProcessor(PREFIX, getDialectProcessorPrecedence() * 10);
    }

    public ComponentIncludeProcessor createIncludeProcessor() {
        return new ComponentIncludeProcessor(PREFIX, getDialectProcessorPrecedence() * 10 + 1);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        HashSet<IProcessor> processors = new HashSet<>();
        processors.add(createReplaceProcessor());
        processors.add(createIncludeProcessor());

//        processors.forEach(it -> ((AbstractComponentProcessor) it).setTemplateResolvers(templateResolvers));

        return processors;
    }

    public ComponentDialect() {
        super("Component Dialect", PREFIX, 900);
    }
}

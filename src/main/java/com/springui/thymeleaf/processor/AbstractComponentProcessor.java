package com.springui.thymeleaf.processor;

import com.springui.ui.Component;
import com.springui.ui.Layout;
import com.springui.ui.UI;
import com.springui.web.TemplateUtils;
import org.springframework.ui.context.Theme;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.NoOpToken;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Map;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractComponentProcessor extends AbstractAttributeTagProcessor {

    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag tag,
                             final AttributeName currentAttributeName,
                             String attributeValue,
                             IElementTagStructureHandler structureHandler) {

        IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(context.getConfiguration());
        IStandardExpression expression = expressionParser.parseExpression(context, attributeValue);
        Object result = expression.execute(context);

        if (result == null || NoOpToken.class.isAssignableFrom(result.getClass())) {
            return;
        }

        String template;

        if (result instanceof Component) {
            Component component = (Component) result;
//            UI ui = component.getUi();
            UI ui = UI.getCurrent();
            Theme theme = ui.getTheme();
            template = TemplateUtils.resolveTemplate(theme.getName(), component);

            if (StringUtils.isEmpty(template)) {
                throw new TemplateProcessingException(String.format("Unable to resolve template for expression %s",
                        expression.getStringRepresentation()));
            }

            structureHandler.setLocalVariable("self", result);

            if (component instanceof Layout) {
                Layout layout = (Layout) component;
                Map<String, Component> components = layout.getComponents();
                for (Map.Entry<String, Component> entry : components.entrySet()) {
                    structureHandler.setLocalVariable(entry.getKey(), entry.getValue());
                }
            }

            String newAttributeName = "th:" + currentAttributeName.getAttributeName();
            structureHandler.replaceAttribute(currentAttributeName, newAttributeName, template);
        } else {
            throw new RuntimeException("No component");
        }
    }

    protected AbstractComponentProcessor(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence, boolean removeAttribute) {
        super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence, removeAttribute);
    }
}

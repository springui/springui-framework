package com.springui.thymeleaf.processor;

import com.springui.ui.Component;
import com.springui.ui.CustomLayout;
import com.springui.ui.UI;
import com.springui.util.BeanFactoryUtils;
import com.springui.web.TemplateResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.context.Theme;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring4.context.SpringContextUtils;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.NoOpToken;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractComponentProcessor extends AbstractAttributeTagProcessor {

    private HttpServletRequest getRequest(ITemplateContext context) {
        return ((WebEngineContext) context).getRequest();
    }

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

        if (result instanceof Component) {
            Component component = (Component) result;
//            UI ui = component.getUi();

            HttpServletRequest request = getRequest(context);
            UI ui = UI.forRequest(request);
            Theme theme = ui.getTheme();

            ApplicationContext applicationContext = SpringContextUtils.getApplicationContext(context);

//            TemplateResolver templateResolver = applicationContext.getBean(TemplateResolver.class);
//            template = templateResolver.resolveTemplate(component);
            Collection<TemplateResolver> templateResolvers = BeanFactoryUtils
                    .getSingletonBeans(applicationContext, TemplateResolver.class);

            String template = templateResolvers.stream()
                    .sorted(Comparator.comparingInt(TemplateResolver::getPriority))
                    .filter(it -> it.accept(theme.getName()))
                    .map(it -> it.resolveTemplate(theme.getName(), component))
                    .filter(Objects::nonNull)
                    .findFirst().orElse(null);

            if (StringUtils.isEmpty(template)) {
                throw new TemplateProcessingException(String.format("Unable to resolve template for expression %s",
                        expression.getStringRepresentation()));
            }

            structureHandler.setLocalVariable("self", result);

            if (component instanceof CustomLayout) {
                CustomLayout layout = (CustomLayout) component;
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

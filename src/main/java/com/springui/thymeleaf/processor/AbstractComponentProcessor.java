package com.springui.thymeleaf.processor;

import com.springui.beans.BeanFactoryUtils;
import com.springui.ui.Component;
import com.springui.ui.ComponentsMapLayout;
import com.springui.ui.Layout;
import com.springui.web.TemplateNameResolver;
import com.springui.web.TemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ThemeResolver;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractComponentProcessor extends AbstractAttributeTagProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractComponentProcessor.class);

    private Map<Class<? extends Component>, String> templateNameCache = new HashMap<>();

    private HttpServletRequest getRequest(ITemplateContext context) {
        return ((WebEngineContext) context).getRequest();
    }

    protected String resolveTemplateName(ITemplateContext context, Component component) {
        boolean cacheable = !(component instanceof Layout);

        String templateName = cacheable ? templateNameCache.get(component.getClass()) : null;
        if (templateName == null) {
            ApplicationContext applicationContext = SpringContextUtils.getApplicationContext(context);
            ThemeResolver themeResolver = applicationContext.getBean(ThemeResolver.class);

            HttpServletRequest request = getRequest(context);
            String themeName = themeResolver.resolveThemeName(request);
            ThemeSource themeSource = applicationContext.getBean(ThemeSource.class);
            Theme theme = themeSource.getTheme(themeName);

            Collection<TemplateNameResolver> templateNameResolvers =
                    BeanFactoryUtils.getSingletonBeans(applicationContext, TemplateNameResolver.class);
            templateName = TemplateUtils.resolveTemplateName(theme, component, templateNameResolvers);
            if (templateName != null) {
                LOG.debug("Template [{}] resolved for class [{}]", templateName, component.getClass());
                if (cacheable) {
                    templateNameCache.put(component.getClass(), templateName);
                }
            }
        }

        return templateName;
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
            structureHandler.setLocalVariable("self", component);

            String templateName = resolveTemplateName(context, component);
            if (StringUtils.isEmpty(templateName)) {
                throw new TemplateProcessingException(String.format("Unable to resolve template for expression %s",
                        expression.getStringRepresentation()));
            }

            if (component instanceof ComponentsMapLayout) {
                ComponentsMapLayout container = (ComponentsMapLayout) component;
                for (String componentName : container.getComponentNames()) {
                    Component containerComponent = container.getComponent(componentName);
                    structureHandler.setLocalVariable(componentName, containerComponent);
                }
            }

            String newAttributeName = "th:" + currentAttributeName.getAttributeName();
            structureHandler.replaceAttribute(currentAttributeName, newAttributeName, templateName);
        } else {
            throw new RuntimeException("No component");
        }
    }

    protected AbstractComponentProcessor(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence, boolean removeAttribute) {
        super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence, removeAttribute);
    }
}

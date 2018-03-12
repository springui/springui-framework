package com.springui.configuration;

import com.springui.beans.UIScope;
import com.springui.thymeleaf.UIDialect;
import com.springui.web.DefaultTemplateNameResolver;
import com.springui.web.servlet.ViewMappingHandlerMapping;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.ResourceBundleThemeSource;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.theme.FixedThemeResolver;

/**
 * @author Stephan Grundner
 */
@ComponentScan("com.springui")
@EnableConfigurationProperties
public class UIConfigurationSupport implements BeanFactoryPostProcessor {

//    @Bean
//    public EmbeddedServletContainerCustomizer customizer() {
//        return container -> {
//            if (container instanceof TomcatEmbeddedServletContainerFactory) {
//                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
//                tomcat.addContextCustomizers(context -> context.setCookieProcessor(new LegacyCookieProcessor()));
//            }
//        };
//    }

    @Bean
    @ConditionalOnMissingBean
    protected ThemeResolver themeResolver() {
        FixedThemeResolver themeResolver = new FixedThemeResolver();
        themeResolver.setDefaultThemeName("twbs4");

        return themeResolver;
    }

    @Bean
    @ConditionalOnMissingBean
    protected ThemeSource themeSource() {
        ResourceBundleThemeSource themeSource = new ResourceBundleThemeSource();
        themeSource.setFallbackToSystemLocale(true);

        return themeSource;
    }

    @Bean
    protected ViewMappingHandlerMapping viewMappingHandlerMapping() {
        ViewMappingHandlerMapping handlerMapping = new ViewMappingHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setAlwaysUseFullPath(true);

        return handlerMapping;
    }

    @Bean
    @Primary
    protected DefaultTemplateNameResolver defaultTemplateNameResolver() {
        return new DefaultTemplateNameResolver();
    }

    @Bean
    protected UIDialect uiDialect() {
        return new UIDialect();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerScope("ui", new UIScope());
    }
}

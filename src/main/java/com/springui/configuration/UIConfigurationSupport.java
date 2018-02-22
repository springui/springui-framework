package com.springui.configuration;

import com.springui.thymeleaf.UIDialect;
import com.springui.view.AnnotationConfigViewMappingRegistryFactory;
import com.springui.view.ViewMappingRegistryFactory;
import com.springui.web.AnnotationConfigUIMappingRegistry;
import com.springui.web.UIMappingRegistry;
import com.springui.web.UIRequestHandler;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.ResourceBundleThemeSource;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.theme.FixedThemeResolver;

import java.util.Properties;

/**
 * @author Stephan Grundner
 */
@ComponentScan("com.springui")
@EnableSpringHttpSession
@EnableConfigurationProperties
public class UIConfigurationSupport implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    protected ThemeResolver themeResolver() {
        FixedThemeResolver themeResolver = new FixedThemeResolver();
        themeResolver.setDefaultThemeName("bootstrap");

        return themeResolver;
    }

    @Bean
    protected ThemeSource themeSource() {
        ResourceBundleThemeSource themeSource = new ResourceBundleThemeSource();
        themeSource.setFallbackToSystemLocale(true);

        return themeSource;
    }

    @Bean
    public EmbeddedServletContainerCustomizer customizer() {
        return container -> {
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
                tomcat.addContextCustomizers(context -> context.setCookieProcessor(new LegacyCookieProcessor()));
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(SessionRepository.class)
    protected SessionRepository sessionRepository() {
        return new MapSessionRepository();
    }

    @Bean
    protected UIDialect uiDialect() {
        return new UIDialect();
    }

    @Bean
    @ConditionalOnMissingBean(UIMappingRegistry.class)
    protected UIMappingRegistry uiMappingRegistry() {
        return new AnnotationConfigUIMappingRegistry();
    }

    @Bean
    @ConditionalOnMissingBean(ViewMappingRegistryFactory.class)
    protected ViewMappingRegistryFactory viewMappingRegistryFactory() {
        return new AnnotationConfigViewMappingRegistryFactory();
    }

    @Bean
    protected UIRequestHandler uiRequestHandler() {
        return new UIRequestHandler();
    }

    @Bean
    protected SimpleUrlHandlerMapping urlHandlerMapping(
            UIMappingRegistry uiMappingRegistry,
            UIRequestHandler uiRequestHandler) {

        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setAlwaysUseFullPath(true);
//        handlerMapping.setUseTrailingSlashMatch(true);
        Properties mappings = new Properties();
        uiMappingRegistry.getMappings().forEach(mapping -> {
            mappings.put(mapping.getPattern(), uiRequestHandler);
        });
        handlerMapping.setMappings(mappings);

        return handlerMapping;
    }
}

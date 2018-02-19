package com.springui.configuration;

import com.springui.thymeleaf.UIDialect;
import com.springui.ui.UI;
import com.springui.web.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
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
    @ConditionalOnMissingBean(SessionRepository.class)
    protected SessionRepository sessionRepository() {
        return new MapSessionRepository();
    }

    @Bean
    @ConditionalOnMissingBean(ViewMappingRegistry.class)
    protected ViewMappingRegistry viewMappingRegistry() {
        AnnotationConfigViewMappingRegistry viewMappingRegistry =
                new AnnotationConfigViewMappingRegistry(applicationContext);
        viewMappingRegistry.detect();

        return viewMappingRegistry;
    }

    @Bean
    protected UIDialect uiDialect() {
        return new UIDialect();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnMissingBean(UI.class)
    protected UI ui() {
        return new UI();
    }

    @Bean
    protected UIRequestController uiRequestController() {
        return new UIRequestController();
    }

    @Bean
    protected UIActionController uiActionController() {
        return new UIActionController();
    }

    @Bean
    protected UIUploadController uiUploadController() {
        return new UIUploadController();
    }

    @Bean
    protected SimpleUrlHandlerMapping urlHandlerMapping(
            ViewMappingRegistry viewMappingRegistry,
            UIRequestController uiRequestController,
            UIActionController uiActionController,
            UIUploadController uiUploadController) {

        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setAlwaysUseFullPath(true);
//        handlerMapping.setUseTrailingSlashMatch(true);
        Properties mappings = new Properties();
        viewMappingRegistry.getMappings().forEach((path, viewClass) -> {
            mappings.put(path, uiRequestController);
            mappings.put(path + "/action", uiActionController);
            mappings.put(path + "/upload", uiUploadController);
        });
        handlerMapping.setMappings(mappings);

        return handlerMapping;
    }
}

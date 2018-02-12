package com.springui.configuration;

import com.springui.thymeleaf.UIDialect;
import com.springui.web.AnnotationConfigViewMappingRegistry;
import com.springui.web.UIController;
import com.springui.web.ViewMappingRegistry;
import com.springui.web.WildcardUIController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.ResourceBundleThemeSource;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.theme.FixedThemeResolver;

/**
 * @author Stephan Grundner
 */
@ComponentScan("com.springui")
@EnableConfigurationProperties
public class UIConfigurationSupport extends WebMvcConfigurerAdapter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    protected UIDialect componentDialect() {
        UIDialect dialect = new UIDialect();

        return dialect;
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
    @Order(0)
    @ConditionalOnMissingBean(UIController.class)
    protected UIController uiController() {
        return new WildcardUIController();
    }
}

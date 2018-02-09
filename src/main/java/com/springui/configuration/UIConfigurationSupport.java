package com.springui.configuration;

import com.springui.thymeleaf.UIDialect;
import com.springui.web.UIRequestInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.ResourceBundleThemeSource;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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
    protected UIRequestInterceptor uiRequestInterceptor() {
        return new UIRequestInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        UIRequestInterceptor uiRequestInterceptor =
                applicationContext.getBean(UIRequestInterceptor.class);
        registry.addInterceptor(uiRequestInterceptor);
    }
}

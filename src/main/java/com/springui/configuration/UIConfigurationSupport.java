package com.springui.configuration;

import com.springui.thymeleaf.UIDialect;
import com.springui.web.ResourceBundleUIThemeSource;
import com.springui.web.UIThemeSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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

    @ConditionalOnMissingBean(ThemeResolver.class)
    @Bean
    protected ThemeResolver bootstrapThemeResolver() {
        FixedThemeResolver themeResolver = new FixedThemeResolver();
        themeResolver.setDefaultThemeName("bootstrap");

        return themeResolver;
    }

    @ConditionalOnMissingBean(UIThemeSource.class)
    @Bean
    protected UIThemeSource themeSource() {
        ResourceBundleUIThemeSource themeSource = new ResourceBundleUIThemeSource();

        return themeSource;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) { }
}

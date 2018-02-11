package com.springui.configuration;

import com.springui.thymeleaf.UIDialect;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
public class UIConfigurationSupport extends WebMvcConfigurerAdapter {

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
}

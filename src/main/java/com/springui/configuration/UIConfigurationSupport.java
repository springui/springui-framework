package com.springui.configuration;

import com.springui.thymeleaf.UIDialect;
import com.springui.ui.UI;
import com.springui.util.BeanUtils;
import com.springui.util.ClassLoaderUtils;
import com.springui.web.PathUtils;
import com.springui.ui.UIPathMapping;
import com.springui.web.UIRegistry;
import com.springui.web.UIRequestInterceptor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
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
//        themeSource.setBasenamePrefix();
        themeSource.setFallbackToSystemLocale(true);

        return themeSource;
    }

    @Bean
    protected UIRequestInterceptor uiRequestInterceptor() {
        return new UIRequestInterceptor();
    }

    @Bean
    protected UIRegistry uiRegistry() {
        UIRegistry uiRegistry = new UIRegistry();

        String[] beanNames = applicationContext.getBeanNamesForAnnotation(UIPathMapping.class);

        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = BeanUtils.getBeanDefinition(beanName,
                    applicationContext.getAutowireCapableBeanFactory());

            ClassLoader classLoader = applicationContext.getClassLoader();
            String controllerClassName = beanDefinition.getBeanClassName();
            Class<? extends UI> uiClass = ClassLoaderUtils.loadClass(classLoader, controllerClassName, UI.class);

            if (uiClass != null) {
                UIPathMapping mapping = AnnotationUtils.findAnnotation(uiClass, UIPathMapping.class);
                String path = (String) AnnotationUtils.getValue(mapping, "path");
                path = PathUtils.normalize(path);
                uiRegistry.registerUiClass(uiClass, path);
            }
        }

        return uiRegistry;
    }

//    @Bean
//    protected UIControllerRegistry uiControllerRegistry() {
//        UIControllerRegistry registry = new UIControllerRegistry();
//
//        String[] beanNames = applicationContext.getBeanNamesForAnnotation(UIControllerMapping.class);
//
//        for (String beanName : beanNames) {
//            BeanDefinition beanDefinition = BeanUtils.getBeanDefinition(beanName,
//                    applicationContext.getAutowireCapableBeanFactory());
//
//            ClassLoader classLoader = applicationContext.getClassLoader();
//            String controllerClassName = beanDefinition.getBeanClassName();
//            Class<? extends UIController> controllerClass =
//                    ClassLoaderUtils.loadClass(classLoader, controllerClassName, UIController.class);
//
//            if (controllerClass != null) {
//                UIControllerMapping mapping = AnnotationUtils.findAnnotation(controllerClass, UIControllerMapping.class);
//                String path = (String) AnnotationUtils.getValue(mapping, "path");
//                path = PathUtils.normalize(path);
//                registry.registerController(controllerClass, path);
//            }
//        }
//
//        return registry;
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        UIRequestInterceptor uiRequestInterceptor = applicationContext.getBean(UIRequestInterceptor.class);
        registry.addInterceptor(uiRequestInterceptor);
    }
}

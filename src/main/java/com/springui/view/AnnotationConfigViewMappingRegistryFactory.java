package com.springui.view;

import com.springui.ui.UI;
import com.springui.util.BeanFactoryUtils;
import com.springui.util.ClassLoaderUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Stephan Grundner
 */
public class AnnotationConfigViewMappingRegistryFactory implements ViewMappingRegistryFactory, ApplicationContextAware, InitializingBean {

    private final Set<Class<? extends View>> annotatedViewClasses = new HashSet<>();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ViewMappingRegistry createViewMappingRegistry(UI ui) {
        ViewMappingRegistry viewMappingRegistry = new ViewMappingRegistry();
        for (Class<? extends View> viewClass : annotatedViewClasses) {
            ViewMapping annotation = AnnotationUtils.findAnnotation(viewClass, ViewMapping.class);
            String[] paths = (String[]) AnnotationUtils.getValue(annotation, "path");
            Stream.of(paths).forEach(path -> viewMappingRegistry.registerViewClass(path, viewClass));
        }

        return viewMappingRegistry;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(ViewMapping.class);
        ClassLoader classLoader = applicationContext.getClassLoader();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = BeanFactoryUtils.getBeanDefinition(beanName, applicationContext);
            Class<? extends View> viewClass = ClassLoaderUtils.loadClass(classLoader, beanDefinition.getBeanClassName(), View.class);
            if (viewClass != null) {
                annotatedViewClasses.add(viewClass);
            }
        }
    }
}

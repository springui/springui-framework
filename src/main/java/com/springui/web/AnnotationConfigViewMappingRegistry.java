package com.springui.web;

import com.springui.ui.View;
import com.springui.util.BeanUtils;
import com.springui.util.ClassLoaderUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author Stephan Grundner
 */
public class AnnotationConfigViewMappingRegistry extends ViewMappingRegistry {

    private final ApplicationContext applicationContext;

    public void detect() {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(ViewMapping.class);
        ClassLoader classLoader = applicationContext.getClassLoader();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = BeanUtils.getBeanDefinition(beanName, applicationContext);
            Class<? extends View> viewClass = ClassLoaderUtils.loadClass(classLoader, beanDefinition.getBeanClassName(), View.class);
            if (viewClass != null) {
                ViewMapping mapping = AnnotationUtils.findAnnotation(viewClass, ViewMapping.class);
                String[] paths = (String[]) AnnotationUtils.getValue(mapping, "path");
                for (String path : paths) {
                    register(path, viewClass);
                }
            }
        }
    }

    public AnnotationConfigViewMappingRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}

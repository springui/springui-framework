package com.springui.web.servlet;

import com.springui.beans.BeanFactoryUtils;
import com.springui.ui.View;
import com.springui.web.ViewController;
import com.springui.web.ViewMapping;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author Stephan Grundner
 */
public class ViewMappingHandlerMapping extends ViewHandlerMapping {

    @SuppressWarnings("unchecked")
    private static <T> Class<? extends T> loadClass(ClassLoader classLoader, String className, Class<T> assignableClass) {
        try {
            Class<?> loadedClass = classLoader.loadClass(className);
            if (!assignableClass.isAssignableFrom(loadedClass)) {
                throw new IllegalArgumentException(String.format("[%s] is not assignable from [%s]", assignableClass, loadedClass));
            }
            return (Class<? extends T>) loadedClass;
        } catch (ClassNotFoundException e) {}

        return null;
    }

    @Override
    protected void initApplicationContext() throws BeansException {
        super.initApplicationContext();

        ApplicationContext applicationContext = getApplicationContext();

        String[] beanNames = applicationContext.getBeanNamesForAnnotation(ViewMapping.class);
        ClassLoader classLoader = applicationContext.getClassLoader();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = BeanFactoryUtils.getBeanDefinition(beanName, applicationContext);
            Class<? extends View> viewClass = loadClass(classLoader, beanDefinition.getBeanClassName(), View.class);
            if (viewClass != null) {
                ViewMapping annotation = AnnotationUtils.findAnnotation(viewClass, ViewMapping.class);
                String[] paths = (String[]) AnnotationUtils.getValue(annotation, "path");
                for (String path : paths) {
                    registerHandler(path, new ViewController(viewClass));
                }
            }
        }
    }
}

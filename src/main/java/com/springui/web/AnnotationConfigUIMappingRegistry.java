package com.springui.web;

import com.springui.ui.UI;
import com.springui.util.BeanFactoryUtils;
import com.springui.util.ClassLoaderUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author Stephan Grundner
 */
public class AnnotationConfigUIMappingRegistry extends UIMappingRegistry implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(UIMapping.class);
        ClassLoader classLoader = applicationContext.getClassLoader();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = BeanFactoryUtils.getBeanDefinition(beanName, applicationContext);
            Class<? extends UI> uiClass = ClassLoaderUtils.loadClass(classLoader, beanDefinition.getBeanClassName(), UI.class);
            if (uiClass != null) {
                UIMapping mapping = AnnotationUtils.findAnnotation(uiClass, UIMapping.class);
                String[] paths = (String[]) AnnotationUtils.getValue(mapping, "path");
                for (String path : paths) {
//                    register(path, viewClass);
                    registerUiClass(path, uiClass);
                }
            }
        }
    }
}

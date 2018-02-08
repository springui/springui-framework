package com.springui.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author Stephan Grundner
 */
public class BeanUtils {

    public static BeanDefinition getBeanDefinition(String name, ConfigurableListableBeanFactory beanFactory) {
        try {
            return beanFactory.getBeanDefinition(name);
        } catch (NoSuchBeanDefinitionException e) {

            BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();
            if (parentBeanFactory instanceof ConfigurableListableBeanFactory) {
                return getBeanDefinition(name, (ConfigurableListableBeanFactory) parentBeanFactory);
            }

            throw e;
        }
    }

    public static BeanDefinition getBeanDefinition(String name, AutowireCapableBeanFactory beanFactory) {
        return getBeanDefinition(name, (ConfigurableListableBeanFactory) beanFactory);
    }
}

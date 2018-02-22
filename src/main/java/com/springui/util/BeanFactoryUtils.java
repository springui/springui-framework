package com.springui.util;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author Stephan Grundner
 */
public class BeanFactoryUtils {

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

    public static BeanDefinition getBeanDefinition(String name, ApplicationContext applicationContext) {
        return getBeanDefinition(name, (ConfigurableListableBeanFactory) applicationContext.getAutowireCapableBeanFactory());
    }

    public static String getUniqueBeanNameForType(ListableBeanFactory beanFactory, Class<?> type) throws NoUniqueBeanDefinitionException {
        String[] beanNames = beanFactory.getBeanNamesForType(type);
        if (beanNames.length > 1) {
            throw new NoUniqueBeanDefinitionException(type, beanNames);
        }
        return beanNames.length == 1 ? beanNames[0] : null;
    }

    public static <T> T getPrototypeBean(ListableBeanFactory beanFactory, Class<T> type, Object... args) {
        String beanName = getUniqueBeanNameForType(beanFactory, type);
        if (!beanFactory.isPrototype(beanName)) {
            throw new BeanInstantiationException(type, "Prototype bean required");
        }

        if (args.length != 0) {
            return beanFactory.getBean(type, args);
        }

        return beanFactory.getBean(type);
    }

    public static <T> T getSingletonBean(ListableBeanFactory beanFactory, Class<T> type, Object... args) {
        String beanName = getUniqueBeanNameForType(beanFactory, type);
        if (!beanFactory.isSingleton(beanName)) {
            throw new BeanInstantiationException(type, "Singleton bean required");
        }

        return beanFactory.getBean(type, args);
    }
}

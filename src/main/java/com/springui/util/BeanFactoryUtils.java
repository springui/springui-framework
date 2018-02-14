package com.springui.util;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;

/**
 * @author Stephan Grundner
 */
public class BeanFactoryUtils {

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

        return beanFactory.getBean(type, args);
    }

    public static <T> T getSingletonBean(ListableBeanFactory beanFactory, Class<T> type, Object... args) {
        String beanName = getUniqueBeanNameForType(beanFactory, type);
        if (!beanFactory.isSingleton(beanName)) {
            throw new BeanInstantiationException(type, "Singleton bean required");
        }

        return beanFactory.getBean(type, args);
    }
}

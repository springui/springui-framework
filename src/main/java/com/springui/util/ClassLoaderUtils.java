package com.springui.util;

/**
 * @author Stephan Grundner
 */
public class ClassLoaderUtils {

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> loadClass(ClassLoader classLoader, String className, Class<T> assignableClass) {
        try {
            Class<?> loadedClass = classLoader.loadClass(className);
            if (!assignableClass.isAssignableFrom(loadedClass)) {
                throw new IllegalArgumentException(String.format("[%s] is not assignable from [%s]", assignableClass, loadedClass));
            }
            return (Class<? extends T>) loadedClass;
        } catch (ClassNotFoundException e) {}

        return null;
    }
}

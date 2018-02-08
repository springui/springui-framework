package com.springui.ui;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Stephan Grundner
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UIPathMapping {

    @AliasFor("path")
    String value() default "";

    @AliasFor("value")
    String path() default "";
}

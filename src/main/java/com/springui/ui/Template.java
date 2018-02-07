package com.springui.ui;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Stephan Grundner
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Template {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}

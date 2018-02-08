package com.springui.ui;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author Stephan Grundner
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface UIPathMapping {

    @AliasFor("path")
    String value() default "";

    @AliasFor("value")
    String path() default "";
}

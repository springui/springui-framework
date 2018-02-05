package com.springui.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Stephan Grundner
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(UIConfigurationSupport.class)
public @interface EnableUI {

}

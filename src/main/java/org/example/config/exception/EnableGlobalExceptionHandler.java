package org.example.config.exception;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by Administrator
 * Data 1:36 2021/12/26 星期日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(GlobalExceptionConfig.class)
public @interface EnableGlobalExceptionHandler {
}

package io.java.datasource.annotation;

import java.lang.annotation.*;

/**
 *
 * @author Jiasi Lu
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    String value() default "";
}

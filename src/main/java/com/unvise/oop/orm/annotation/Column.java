package com.unvise.oop.orm.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Documented
public @interface Column {
    String name() default "";
    boolean primaryKey() default false;
}

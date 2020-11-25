package ru.javawebinar.topjava.util;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTime {
    String info() default "LocalDate";
}
package com.elakov.rangiffler.jupiter.annotation.creation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
public @interface CreatePhoto {

    String photoPath() default "";

    String countryCode() default "";

    String description() default "";

}

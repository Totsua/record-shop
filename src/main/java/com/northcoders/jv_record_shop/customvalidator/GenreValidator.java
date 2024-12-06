package com.northcoders.jv_record_shop.customvalidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GenreValidatorConstraint.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,ElementType.TYPE_USE})
@NotNull
public @interface GenreValidator {

    Class<? extends Enum<?>> enumClass();
    String message() default "must be any of Genre {ROCK,HIPHOP,RAP,JAZZ,POP,UNKNOWN}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


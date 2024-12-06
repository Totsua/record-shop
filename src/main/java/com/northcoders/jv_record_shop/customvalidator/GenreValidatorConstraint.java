package com.northcoders.jv_record_shop.customvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenreValidatorConstraint implements ConstraintValidator<GenreValidator,  String> {
    Set<String> values;

    @Override
    public void initialize(GenreValidator constraintAnnotation) {
        values = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        return values.contains(value);

    }

}

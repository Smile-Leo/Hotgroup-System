package com.hotgroup.commons.validator.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * @author Lzw
 * @date 2021/4/20.
 */
public class IntValidator implements ConstraintValidator<IntSelection, Integer> {

    private int[] value;

    @Override
    public void initialize(IntSelection constraintAnnotation) {
        this.value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        return Arrays.stream(value).anyMatch(s1 -> Integer.valueOf(s1).equals(s));
    }
}

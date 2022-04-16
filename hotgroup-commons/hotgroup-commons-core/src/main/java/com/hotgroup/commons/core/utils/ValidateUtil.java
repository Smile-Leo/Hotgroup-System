package com.hotgroup.commons.core.utils;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author Lzw
 * @date 2021/4/19.
 */
public class ValidateUtil {
    private ValidateUtil() {
    }

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validate(T var1, Class<?>... var2) {
        Set<ConstraintViolation<T>> validate = VALIDATOR.validate(var1, var2);
        if (!CollectionUtils.isEmpty(validate)) {
            throw new IllegalArgumentException(validate.iterator().next().getMessage());
        }
    }
}

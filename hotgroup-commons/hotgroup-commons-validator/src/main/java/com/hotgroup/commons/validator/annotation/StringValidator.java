package com.hotgroup.commons.validator.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Lzw
 * @date 2021/4/20.
 */
public class StringValidator implements ConstraintValidator<StringSelection, String> {

    private List<String> value;
    private boolean multipleChoice;

    @Override
    public void initialize(StringSelection constraintAnnotation) {
        this.value = Arrays.stream(constraintAnnotation.value()).map(String::toUpperCase).collect(Collectors.toList());
        this.multipleChoice = constraintAnnotation.multipleChoice();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        String val = s.toUpperCase(Locale.ROOT);

        if (multipleChoice) {
            String[] split = val.split(",");
            for (String s1 : split) {
                if (!value.contains(s1)) {
                    return false;
                }
            }
            return true;
        }

        return value.contains(val);
    }
}

package com.hotgroup.commons.validator.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lzw
 * @date 2021/4/20.
 */
public class EnumValidator implements ConstraintValidator<EnumSelection, String> {

    private List<String> values;
    private boolean multipleChoice;

    @Override
    public void initialize(EnumSelection constraintAnnotation) {
        Enum<?>[] enumConstants = constraintAnnotation.value().getEnumConstants();

        this.values = Arrays.stream(enumConstants).map(anEnum -> {
            try {
                return (String) anEnum.getDeclaringClass().getMethod(constraintAnnotation.method()).invoke(anEnum);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException("@EnumSelection属性设置有误");
            }
        }).collect(Collectors.toList());
        this.multipleChoice = constraintAnnotation.multipleChoice();
    }

    @Override
    public boolean isValid(String o, ConstraintValidatorContext constraintValidatorContext) {
        if (o == null) {
            return true;
        }

        if (multipleChoice) {
            String[] split = o.split(",");
            for (String s : split) {
                if (!values.contains(s)) {
                    return false;
                }
            }
            return true;
        }

        return values.contains(o);

    }
}

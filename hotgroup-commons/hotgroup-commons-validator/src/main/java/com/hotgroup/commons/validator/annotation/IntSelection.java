package com.hotgroup.commons.validator.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Lzw
 * @date 2021/4/20.
 */
@Documented
@Constraint(validatedBy = IntValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntSelection {


    String message() default "选择的数据不匹配";

    int[] value();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

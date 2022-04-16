package com.hotgroup.commons.validator.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Lzw
 * @date 2021/4/20.
 */
@Documented
@Constraint(validatedBy = StringValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringSelection {


    String message() default "选择的数据不匹配";

    String[] value();

    boolean multipleChoice() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

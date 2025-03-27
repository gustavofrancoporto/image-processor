package com.bix.imageprocessor.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ImageTransformParamsRequiredConstraintValidator.class)
@Documented
public @interface ImageTransformParamsRequired {

    String message() default "{com.bix.imageprocessor.web.validation.ImageTransformParamsRequiredConstraintValidator.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 02:00 PM (ICT)
 */
package com.vinn.bookingSystem.config.annotations;

import com.vinn.bookingSystem.config.validators.GenderValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GenderValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGender {
    String message() default "Gender must be a number between 1 (Male), 2 (Female), or 3 (Other).";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

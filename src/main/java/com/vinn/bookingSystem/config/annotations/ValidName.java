/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 02:40 PM (ICT)
 */
package com.vinn.bookingSystem.config.annotations;

import com.vinn.bookingSystem.config.validators.NameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {
    String message() default "Invalid name.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

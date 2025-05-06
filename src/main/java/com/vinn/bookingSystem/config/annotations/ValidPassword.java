/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 02:10 PM (ICT)
 */
package com.vinn.bookingSystem.config.annotations;

import com.vinn.bookingSystem.config.validators.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Invalid password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName() default "Password";
}


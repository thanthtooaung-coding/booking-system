/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 05:50 PM (ICT)
 */
package com.vinn.bookingSystem.config.annotations;

import com.vinn.bookingSystem.config.validators.OtpValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OtpValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOtp {
    String message() default "OTP must be a 6-digit number.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

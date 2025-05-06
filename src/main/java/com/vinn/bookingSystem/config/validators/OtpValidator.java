/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 05:52 PM (ICT)
 */
package com.vinn.bookingSystem.config.validators;

import com.vinn.bookingSystem.config.annotations.ValidOtp;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OtpValidator implements ConstraintValidator<ValidOtp, String> {

    @Override
    public boolean isValid(final String otp, final ConstraintValidatorContext context) {
        if (otp == null || otp.trim().isEmpty()) {
            return buildViolation(context, "OTP is required.");
        }

        if (!otp.matches("\\d{6}")) {
            return buildViolation(context, "OTP must be exactly 6 digits.");
        }

        return true;
    }

    private boolean buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
               .addConstraintViolation();
        return false;
    }
}

/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 02:05 PM (ICT)
 */
package com.vinn.bookingSystem.config.validators;

import com.vinn.bookingSystem.config.annotations.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private String fieldName;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) {
            return buildViolation(context,fieldName + " is required");
        }

        if (password.length() < 8) {
            return buildViolation(context, fieldName + " must be at least 8 characters long");
        }

        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$")) {
            return buildViolation(context, fieldName + " must include uppercase, lowercase, number, and special character");
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

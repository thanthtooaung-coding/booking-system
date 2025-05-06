/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 02:15 PM (ICT)
 */
package com.vinn.bookingSystem.config.validators;

import com.vinn.bookingSystem.config.annotations.ValidGender;
import com.vinn.bookingSystem.features.user.entity.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<ValidGender, Integer> {

    public boolean isValid(final Integer value, final ConstraintValidatorContext context) {
        return Gender.isValidValue(value);
    }
}

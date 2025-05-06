/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 05:45 PM (ICT)
 */
package com.vinn.bookingSystem.security.dto;

import com.vinn.bookingSystem.config.annotations.ValidPassword;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordRequest {

    @ValidPassword(fieldName = "New password")
    private String newPassword;

    @ValidPassword(fieldName = "Confirm password")
    private String confirmPassword;
}
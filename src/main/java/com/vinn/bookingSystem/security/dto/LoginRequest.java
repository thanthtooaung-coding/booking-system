/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:17 AM (ICT)
 */
package com.vinn.bookingSystem.security.dto;

import com.vinn.bookingSystem.config.annotations.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    @ValidPassword
    private String password;
}

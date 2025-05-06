/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:18 AM (ICT)
 */
package com.vinn.bookingSystem.security.dto;

import com.vinn.bookingSystem.config.annotations.ValidGender;
import com.vinn.bookingSystem.config.annotations.ValidName;
import com.vinn.bookingSystem.config.annotations.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    @ValidName
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    @ValidPassword
    private String password;

    @ValidGender
    private Integer gender;
}
/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:19 AM (ICT)
 */
package com.vinn.bookingSystem.security.service;

import com.vinn.bookingSystem.config.response.dto.ApiResponse;
import com.vinn.bookingSystem.security.dto.LoginRequest;
import com.vinn.bookingSystem.security.dto.RegisterRequest;
import com.vinn.bookingSystem.security.dto.ResetPasswordRequest;

public interface AuthService {
    ApiResponse authenticateUser(final LoginRequest loginRequest, final String routeName, String browserName, String pageName);

    ApiResponse registerUser(final RegisterRequest registerRequest);

    void logout(final String accessToken);
//
//    ApiResponse refreshToken(final String refreshToken);
//
    ApiResponse getCurrentUser(final String authHeader, final String routeName, final String browserName, final String pageName);

    ApiResponse changePassword(final String email);

    ApiResponse verifyOtp(final String otp);
//
    ApiResponse resetPassword(final ResetPasswordRequest resetPasswordRequest);
//
//    ApiResponse updateUser(final String authHeader, final UserDto userDto);
}

/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:16 AM (ICT)
 */
package com.vinn.bookingSystem.security.controller;

import com.vinn.bookingSystem.config.exceptions.UnauthorizedException;
import com.vinn.bookingSystem.config.response.dto.ApiResponse;
import com.vinn.bookingSystem.config.request.RequestUtils;
import com.vinn.bookingSystem.config.response.utils.ResponseUtils;
import com.vinn.bookingSystem.security.dto.*;
import com.vinn.bookingSystem.security.service.AuthService;
import com.vinn.bookingSystem.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.path}/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    public final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @Validated @RequestBody final LoginRequest loginRequest,
            final HttpServletRequest request,
            @RequestParam(required = false) final String routeName,
            @RequestParam(required = false) final String browserName,
            @RequestParam(required = false) final String pageName
    ) {
        log.info("Received login attempt for email: {}", loginRequest.getEmail());

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        ApiResponse response = authService.authenticateUser(loginRequest, routeName, browserName, pageName);

        if (response.getSuccess() == 1) {
            log.info("Login successful for user: {}", loginRequest.getEmail());
        } else {
            log.warn("Login failed for user: {}", loginRequest.getEmail());
        }

        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            HttpServletRequest request) {
        log.info("Received logout request");

        final double requestStartTime = RequestUtils.extractRequestStartTime(request);

        if ((accessToken == null || !accessToken.startsWith("Bearer "))) {

            log.warn("Invalid or missing tokens in logout request");
            throw new UnauthorizedException("Invalid or missing authorization tokens.");
        }

        try {
            this.authService.logout(accessToken);
            final ApiResponse response = ApiResponse.builder()
                    .success(1)
                    .code(200)
                    .data(true)
                    .message("Logout successful")
                    .build();

            log.info("User logged out successfully");

            return ResponseUtils.buildResponse(request, response, requestStartTime);
        } catch (UnauthorizedException ex) {
            log.warn("Logout failed due to security reasons: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error during logout", ex);
            throw new RuntimeException("An error occurred during logout.");
        }
    }

    @PostMapping("/register")
//    @DeprecatedRoute(message = "This endpoint is deprecated. Use /new-endpoint instead.")
    public ResponseEntity<ApiResponse> register(@Validated @RequestBody final RegisterRequest registerRequest,
            final HttpServletRequest request) {
        log.info("Received registration request for email: {}", registerRequest.getEmail());

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        ApiResponse response = authService.registerUser(registerRequest);

        if (response.getSuccess() == 1) {
            log.info("User registered successfully: {}", registerRequest.getEmail());
        } else {
            log.warn("Registration failed for email: {}", registerRequest.getEmail());
        }

        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }

//    @PostMapping("/refresh")
//    public ResponseEntity<ApiResponse> refresh(@Validated @RequestBody RefreshTokenData refreshTokenData,
//            HttpServletRequest request) {
//        log.info("Received token refresh request");
//
//        double requestStartTime = RequestUtils.extractRequestStartTime(request);
//
//        ApiResponse response = authService.refreshToken(refreshTokenData.getRefreshToken());
//
//        if (response.getSuccess() == 1) {
//            log.info("Token refreshed successfully");
//        } else {
//            log.warn("Token refresh failed");
//        }
//
//        return ResponseUtils.buildResponse(request, response, requestStartTime);
//    }
//
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentUser(
            @RequestHeader("Authorization") final String authHeader,
            @RequestParam(required = false) final String routeName,
            @RequestParam(required = false) final String browserName,
            @RequestParam(required = false) final String pageName,
            HttpServletRequest request) {
        log.info("Fetching current authenticated user");

        final double requestStartTime = System.currentTimeMillis();
        final ApiResponse response = this.authService.getCurrentUser(authHeader, routeName, browserName, pageName);

        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> forgotPassword(
            @Validated @RequestBody final ChangePasswordRequest changePasswordRequest,
            HttpServletRequest httpRequest) {
        log.info("Received change password request");
        final double requestStartTime = RequestUtils.extractRequestStartTime(httpRequest);

        final ApiResponse response = this.authService.changePassword(changePasswordRequest.getEmail());
        return ResponseUtils.buildResponse(httpRequest, response, requestStartTime);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(
            @Validated @RequestBody final VerifyOtpRequest verifyOtpRequest,
            HttpServletRequest httpRequest) {
        log.info("Received OTP verification request");
        double requestStartTime = RequestUtils.extractRequestStartTime(httpRequest);

        final ApiResponse response = this.authService.verifyOtp(verifyOtpRequest.getOtp());
        return ResponseUtils.buildResponse(httpRequest, response, requestStartTime);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @Validated @RequestBody final ResetPasswordRequest resetPasswordRequest,
            final HttpServletRequest httpRequest) {
        log.info("Received password reset request");
        final double requestStartTime = RequestUtils.extractRequestStartTime(httpRequest);

        final ApiResponse response = this.authService.resetPassword(resetPasswordRequest);
        return ResponseUtils.buildResponse(httpRequest, response, requestStartTime);
    }
//
//    @PatchMapping("/updateMe")
//    public ResponseEntity<ApiResponse> updateUser(
//            @RequestHeader("Authorization") String authHeader,
//            @RequestBody UserDto userDto,
//            HttpServletRequest request) {
//
//        log.info("Updating user information");
//
//        double requestStartTime = System.currentTimeMillis();
//        ApiResponse response = authService.updateUser(authHeader, userDto);
//
//        return ResponseUtils.buildResponse(request, response, requestStartTime);
//    }

}

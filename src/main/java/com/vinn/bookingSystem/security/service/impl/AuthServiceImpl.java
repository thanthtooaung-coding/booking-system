/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:20 AM (ICT)
 */
package com.vinn.bookingSystem.security.service.impl;

import com.vinn.bookingSystem.config.exceptions.UnauthorizedException;
import com.vinn.bookingSystem.config.response.dto.ApiResponse;
import com.vinn.bookingSystem.config.service.EmailService;
import com.vinn.bookingSystem.config.utils.DtoUtil;
import com.vinn.bookingSystem.config.utils.EntityUtil;
import com.vinn.bookingSystem.features.user.dto.response.UserDto;
import com.vinn.bookingSystem.features.user.entity.Gender;
import com.vinn.bookingSystem.features.user.entity.User;
import com.vinn.bookingSystem.features.user.repository.UserRepository;
import com.vinn.bookingSystem.features.user.utils.UserUtil;
import com.vinn.bookingSystem.security.dto.LoginRequest;
import com.vinn.bookingSystem.security.dto.RegisterRequest;
import com.vinn.bookingSystem.security.dto.ResetPasswordRequest;
import com.vinn.bookingSystem.security.dto.VerifyEmailRequest;
import com.vinn.bookingSystem.security.service.AuthService;
import com.vinn.bookingSystem.security.service.JwtService;
import com.vinn.bookingSystem.security.utils.AuthUtil;
import com.vinn.bookingSystem.security.utils.OtpUtils;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
//    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final UserUtil userUtil;
//    private final MailService mailService;
    private final AuthUtil authUtil;
    private final EmailService emailService;
//    private final VisitLogService visitLogService;

    private final Map<String, OtpUtils.OtpData> otpStore = new ConcurrentHashMap<>();
    private String emailInProcess;

    @Override
    public ApiResponse authenticateUser(LoginRequest loginRequest, String routeName, String browserName, String pageName) {
        final String identifier = loginRequest.getEmail();
        log.info("Authenticating user with identifier: {}", identifier);

        final Optional<User> userOpt = this.userRepository.findByEmail(identifier);
//                .or(() -> this.userRepository.findByUsername(identifier));

        final User user = userOpt.orElseThrow(() -> {
            log.warn("User not found with identifier: {}", identifier);
//            return new UnauthorizedException("Invalid email/username or password");
            return new UnauthorizedException("Invalid email or password");
        });

        if (!user.isStatus()) {
            log.warn("User is inactive: {}", loginRequest.getEmail());
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Your account has been locked. Please contact your administrator.")
                    .build();
        }

        if (!this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Invalid password for user: {}", loginRequest.getEmail());
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Invalid email or password")
                    .build();
        }

        log.info("User authenticated successfully: {}", loginRequest.getEmail());

        boolean firstTimeLogin = false;

        if(user.isLoginFirstTime()) {
            firstTimeLogin = true;
            user.setLoginFirstTime(false);
            this.userRepository.save(user);
            log.info("User {} logged in for the first time.", user.getName());
        }

        final UserDto userDto = DtoUtil.map(user, UserDto.class, modelMapper);
        userDto.setLoginFirstTime(firstTimeLogin);
        userDto.setGenderId(Gender.fromInt(user.getGender()).getValue());
        userDto.setGenderName(Gender.fromInt(user.getGender()).getCode());

//        Token refreshToken;
//        Optional<Token> refreshTokenOptional = tokenRepository.findByUserAndExpiredAtAfter(user, Instant.now());
//        if (refreshTokenOptional.isPresent()) {
//            log.info("Valid refresh token found for user: {}", loginRequest.getEmail());
//            refreshToken = refreshTokenOptional.get();
//        } else {
//            log.info("Refresh token expired or not found for user: {}, generating new token", loginRequest.getEmail());
//            Map<String, Object> tokenData = authUtil.generateTokens(user, roleName);
//            String newRefreshToken = (String) tokenData.get("refreshToken");
//
//            Instant newExpiryDate = Instant.now().plusSeconds(7 * 24 * 60 * 60);
//
//            refreshToken = Token.builder()
//                    .refreshtoken(newRefreshToken)
//                    .expiredAt(newExpiryDate)
//                    .user(user)
//                    .build();
//
//            tokenRepository.save(refreshToken);
//        }
//
        Map<String, Object> tokenData = authUtil.generateTokens(user);
//
//        assert refreshTokenOptional.isPresent();
//        TokenDto tokenDto = DtoUtil.map(refreshToken, TokenDto.class, modelMapper);
//
//        VisitLog visitLog = new VisitLog(
//                user,
//                routeName,
//                browserName,
//                pageName
//        );
//
//        visitLogService.save(visitLog);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(
                        Map.of(
                        "currentUser", userDto,
                        "accessToken", tokenData.get("accessToken")
//                        "refreshToken", tokenDto.getRefreshtoken()))
                        )
                )
                .message("You are successfully logged in!")
                .build();
    }

    @Override
    @Transactional
    public ApiResponse registerUser(final RegisterRequest registerRequest) {
        log.info("Registering new user with email: {}", registerRequest.getEmail());

        if (this.userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            log.warn("Email already exists: {}", registerRequest.getEmail());
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.CONFLICT.value())
                    .message("Email is already in use")
                    .build();
        }

        final User newUser = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(this.passwordEncoder.encode(registerRequest.getPassword()))
                .gender(registerRequest.getGender())
                .emailVerified(SendVerifyEmail(registerRequest.getEmail()))
                .build();

        this.userRepository.save(newUser);

        final Map<String, Object> tokenData = this.authUtil.generateTokens(newUser);
//
        final String accessToken = (String) tokenData.get("accessToken");
//        String refreshToken = (String) tokenData.get("refreshToken");
//
//        Instant expiredAt = Instant.now().plus(7, ChronoUnit.DAYS);
//
//        Token token = Token.builder()
//                .user(newUser)
//                .refreshtoken(refreshToken)
//                .expiredAt(expiredAt)
//                .build();
//
//        tokenRepository.save(token);

        log.info("User registered successfully: {}", registerRequest.getEmail());

        final UserDto userDto = DtoUtil.map(newUser, UserDto.class, modelMapper);

        final String token = UUID.randomUUID().toString();
        final VerifyEmailRequest emailRequest = new VerifyEmailRequest(newUser.getEmail(), token);
        this.emailService.sendVerifyEmail(emailRequest);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(
                        Map.of(
                        "user", userDto,
                        "accessToken", accessToken
//                        "refreshToken", refreshToken))
                        )
                )
                .message("You have registered successfully.")
                .build();
    }

    @Override
    public void logout(final String accessToken) {
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            final String token = accessToken.substring(7);
            final Claims claims = this.jwtService.validateToken(token);
            final String userEmail = claims.getSubject();

            final User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UnauthorizedException(
                            "User not found. Cannot proceed with logout."));

            log.debug("Revoking access token for user: {}", user.getEmail());
            this.jwtService.revokeToken(token);
        }

        log.info("User successfully logged out.");
    }

//    @Override
//    public ApiResponse refreshToken(String refreshToken) {
//        log.info("Validating refresh token");
//
//        Claims claims;
//        try {
//            claims = jwtService.validateToken(refreshToken);
//        } catch (TokenExpiredException ex) {
//            log.warn("Invalid refresh token: {}", ex.getMessage());
//            return ApiResponse.builder()
//                    .success(0)
//                    .code(HttpStatus.FORBIDDEN.value())
//                    .data(ex.getMessage())
//                    .message("Invalid or expired refresh token")
//                    .build();
//        }
//
//        String email = claims.getSubject();
//        User user = userRepository.findByEmail(email).orElse(null);
//
//        if (user == null) {
//            log.warn("User not found for refresh token: {}", email);
//            throw new UnauthorizedException("User not found for refresh token");
//        }
//
//        log.info("Generating new access token for user: {}", email);
//
//        Set<Role> roleList = user.getRoles();
//        String roleName = roleList.stream()
//                .map(role -> role.getName().name())
//                .findFirst()
//                .orElse("ROLE_USER");
//
//        String newAccessToken = jwtService.generateToken(ClaimsProvider.generateClaims(user), roleName, email,
//                15 * 60 * 1000);
//
//        return ApiResponse.builder()
//                .success(1)
//                .code(HttpStatus.OK.value())
//                .data(Map.of("accessToken", newAccessToken))
//                .message("Access token refreshed successfully")
//                .build();
//    }

    @Override
    public ApiResponse getCurrentUser(final String authHeader, final String routeName, final String browserName,
                                      final String pageName) {
        UserDto userDto = userUtil.getCurrentUserDto(authHeader);
        User user = EntityUtil.getEntityById(userRepository, userDto.getId());

//        VisitLog visitLog = new VisitLog(
//                user,
//                routeName,
//                browserName,
//                pageName
//        );
//
//        visitLogService.save(visitLog);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of(
                        "user", userDto)
                )
                .message("User retrieved successfully")
                .build();
    }

    @Override
    public ApiResponse changePassword(String email) {
        log.info("Initiating password reset for email: {}", email);

        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("No user found with email: {}", email);
                    return new UnauthorizedException("No user found with this email");
                });

        final String otp = OtpUtils.generateOtp();
        otpStore.put(otp, new OtpUtils.OtpData(email, Instant.now().plus(30, ChronoUnit.MINUTES)));

        try {
            return ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.OK.value())
                    .data(Map.of(
                            "otp", otp)
                    )
                    .message("OTP has been sent to your email")
                    .build();
        } catch (Exception e) {
            log.error("Failed to send OTP email: {}", e.getMessage());
            throw new RuntimeException("Failed to send OTP");
        }
    }

    @Override
    public ApiResponse verifyOtp(final String otp) {
        log.info("Verifying OTP");

        final OtpUtils.OtpData otpData = otpStore.get(otp);
        if (otpData == null || otpData.isExpired()) {
            log.warn("Invalid or expired OTP");
            throw new UnauthorizedException("Invalid or expired OTP");
        }

        emailInProcess = otpData.getEmail();
        otpStore.remove(otp);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(true)
                .message("OTP verified successfully")
                .build();
    }

    @Override
    public ApiResponse resetPassword(final ResetPasswordRequest resetPasswordRequest) {
        if (emailInProcess == null) {
            throw new UnauthorizedException("Please verify OTP first");
        }

        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            throw new UnauthorizedException("Passwords do not match");
        }

        final User user = userRepository.findByEmail(emailInProcess)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        user.setPassword(this.passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        this.userRepository.save(user);

        emailInProcess = null;

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(true)
                .message("Password reset successfully")
                .build();
    }

    public boolean SendVerifyEmail(final String email)
    {
        return true;
    }

}
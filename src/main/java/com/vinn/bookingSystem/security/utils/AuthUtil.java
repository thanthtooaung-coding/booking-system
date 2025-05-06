/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:23 AM (ICT)
 */
package com.vinn.bookingSystem.security.utils;

import com.vinn.bookingSystem.features.user.entity.User;
import com.vinn.bookingSystem.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final JwtService jwtService;

    public Map<String, Object> generateTokens(final User user) {
        log.debug("Generating tokens for user: {}", user.getEmail());

        final String accessToken = this.jwtService.generateToken(ClaimsProvider.generateClaims(user),
                user.getEmail(), 15 * 60 * 1000);
        final String refreshToken = this.jwtService.generateToken(ClaimsProvider.generateClaims(user),
                user.getEmail(), 7 * 24 * 60 * 60 * 1000);

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }
}

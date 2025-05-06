/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:25 AM (ICT)
 */
package com.vinn.bookingSystem.security.service;

import io.jsonwebtoken.Claims;

import java.util.Map;

public interface JwtService {
    Claims validateToken(final String token);

    void revokeToken(final String token);

    String generateToken(final Map<String, Object> claims, final String subject, final long expirationMillis);
}
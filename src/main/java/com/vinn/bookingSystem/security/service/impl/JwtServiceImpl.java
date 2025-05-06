/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:28 AM (ICT)
 */
package com.vinn.bookingSystem.security.service.impl;

import com.vinn.bookingSystem.config.exceptions.TokenExpiredException;
import com.vinn.bookingSystem.config.exceptions.UnauthorizedException;
import com.vinn.bookingSystem.security.service.JwtService;
import com.vinn.bookingSystem.security.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtServiceImpl implements JwtService {

    private final Set<String> revokedTokens = ConcurrentHashMap.newKeySet();

    @Override
    public Claims validateToken(String token) {
        if (!JwtUtil.isTokenValid(token)) {
            throw new TokenExpiredException("Invalid or expired token.");
        }

        if (this.isTokenRevoked(token)) {
            throw new UnauthorizedException("Token has been revoked.");
        }

        return JwtUtil.decodeToken(token);
    }

    @Override
    public void revokeToken(String token) {
        this.revokedTokens.add(token);
    }

    private boolean isTokenRevoked(String token) {
        return this.revokedTokens.contains(token);
    }

    @Override
    public String generateToken(final Map<String, Object> claims, final String subject, final long expirationMillis) {
        return JwtUtil.generateToken(claims, subject, expirationMillis);
    }
}
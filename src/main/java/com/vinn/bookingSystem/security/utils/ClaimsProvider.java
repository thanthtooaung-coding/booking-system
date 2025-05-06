/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:26 AM (ICT)
 */
package com.vinn.bookingSystem.security.utils;

import com.vinn.bookingSystem.features.user.entity.User;

import java.util.HashMap;
import java.util.Map;

public class ClaimsProvider {

    private ClaimsProvider() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, Object> generateClaims(final User user) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        return claims;
    }
}

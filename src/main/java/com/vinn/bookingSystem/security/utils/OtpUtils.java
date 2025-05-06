/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:50 AM (ICT)
 */
package com.vinn.bookingSystem.security.utils;

import lombok.Value;

import java.time.Instant;
import java.util.Random;

public class OtpUtils {

    public static String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    @Value
    public static class OtpData {
        String email;
        Instant expiration;

        public boolean isExpired() {
            return Instant.now().isAfter(expiration);
        }
    }
}
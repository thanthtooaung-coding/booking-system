package com.vinn.bookingSystem.security.dto;

import com.vinn.bookingSystem.config.annotations.ValidOtp;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyOtpRequest {
    @ValidOtp
    private String otp;
}

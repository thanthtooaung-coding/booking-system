package com.vinn.bookingSystem.config.service;

import com.vinn.bookingSystem.security.dto.VerifyEmailRequest;

public interface EmailService {
    boolean sendVerifyEmail(final VerifyEmailRequest request);
}

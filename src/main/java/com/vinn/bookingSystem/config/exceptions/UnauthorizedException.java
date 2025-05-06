/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:42 AM (ICT)
 */
package com.vinn.bookingSystem.config.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

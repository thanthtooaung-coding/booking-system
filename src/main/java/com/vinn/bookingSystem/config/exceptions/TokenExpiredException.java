/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:40 AM (ICT)
 */
package com.vinn.bookingSystem.config.exceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) { super(message); }
}

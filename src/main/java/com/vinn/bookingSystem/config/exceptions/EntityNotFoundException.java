/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:40 AM (ICT)
 */
package com.vinn.bookingSystem.config.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
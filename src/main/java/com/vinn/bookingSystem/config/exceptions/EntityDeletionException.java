/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:40 AM (ICT)
 */
package com.vinn.bookingSystem.config.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntityDeletionException extends RuntimeException {
    public EntityDeletionException(String message) { super(message); }
}

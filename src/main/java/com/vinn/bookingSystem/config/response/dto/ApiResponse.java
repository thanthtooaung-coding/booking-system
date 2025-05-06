/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:43 AM (ICT)
 */
package com.vinn.bookingSystem.config.response.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ApiResponse {
    private int success;
    private int code;
    private Map<String, Object> meta;
    private Object data;
    private String message;
    private double duration;
}
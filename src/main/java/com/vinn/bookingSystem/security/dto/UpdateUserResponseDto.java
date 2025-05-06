/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 11:19 AM (ICT)
 */
package com.vinn.bookingSystem.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserResponseDto {
    private Long id;
    private String email;
    private String username;
    private String createdAt;
    private String updatedAt;
}

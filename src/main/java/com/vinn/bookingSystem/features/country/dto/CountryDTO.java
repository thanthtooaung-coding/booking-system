package com.vinn.bookingSystem.features.country.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    private Long id;
    private String name;
    private String code;
}
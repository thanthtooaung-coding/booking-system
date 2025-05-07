package com.vinn.bookingSystem.features.packages.dto;

import com.vinn.bookingSystem.features.country.dto.CountryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer credits;
    private Integer validityDays;
    private CountryDTO country;
}
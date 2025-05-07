package com.vinn.bookingSystem.features.packages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer credits;
    private Integer validityDays;
    private Long countryId;
}

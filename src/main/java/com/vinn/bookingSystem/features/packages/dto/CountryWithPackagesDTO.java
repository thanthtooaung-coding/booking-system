package com.vinn.bookingSystem.features.packages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryWithPackagesDTO {
    private Long id;
    private String name;
    private String code;
    private List<PackageDTO> packages;
}
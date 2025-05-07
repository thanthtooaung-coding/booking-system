package com.vinn.bookingSystem.features.packages.service;

import com.vinn.bookingSystem.features.packages.dto.CountryWithPackagesDTO;
import com.vinn.bookingSystem.features.packages.dto.PackageDTO;
import com.vinn.bookingSystem.features.packages.dto.PackageRequest;
import com.vinn.bookingSystem.features.userpackages.dto.UserPurchasedPackageDTO;

import java.util.List;

public interface PackageService {
    void createPackage(PackageRequest packageRequest);
    List<PackageDTO> getActivePackagesByCountry(final Long countryId);
    List<PackageDTO> getActivePackagesByCountryCode(final String countryCode);
    List<CountryWithPackagesDTO> getAllActivePackagesByCountry(final boolean isCreated);
    List<UserPurchasedPackageDTO> getUserPurchasedPackages(final String authHeader);
}

package com.vinn.bookingSystem.features.packages.service.impl;

import com.vinn.bookingSystem.config.cache.CacheService;
import com.vinn.bookingSystem.config.exceptions.EntityNotFoundException;
import com.vinn.bookingSystem.config.utils.EntityUtil;
import com.vinn.bookingSystem.features.country.dto.CountryDTO;
import com.vinn.bookingSystem.features.country.entity.Country;
import com.vinn.bookingSystem.features.country.repository.CountryRepository;
import com.vinn.bookingSystem.features.packages.dto.CountryWithPackagesDTO;
import com.vinn.bookingSystem.features.packages.dto.PackageDTO;
import com.vinn.bookingSystem.features.packages.dto.PackageRequest;
import com.vinn.bookingSystem.features.packages.entity.PackageEntity;
import com.vinn.bookingSystem.features.packages.repository.PackageRepository;
import com.vinn.bookingSystem.features.packages.service.PackageService;
import com.vinn.bookingSystem.features.user.dto.response.UserDto;
import com.vinn.bookingSystem.features.user.utils.UserUtil;
import com.vinn.bookingSystem.features.userpackages.dto.UserPurchasedPackageDTO;
import com.vinn.bookingSystem.features.userpackages.entity.UserPackage;
import com.vinn.bookingSystem.features.userpackages.repository.UserPackageRepository;
import com.vinn.bookingSystem.features.userpackages.entity.UserPackageStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final CountryRepository countryRepository;
    private final CacheService cacheService;
    private final UserPackageRepository userPackageRepository;
    private final UserUtil userUtil;

    @Override
    @Transactional
    public void createPackage(final PackageRequest packageRequest) {
        final Country country = EntityUtil.getEntityById(this.countryRepository, packageRequest.getCountryId());

        final PackageEntity packageEntity = PackageEntity.builder()
                .name(packageRequest.getName())
                .description(packageRequest.getDescription())
                .price(packageRequest.getPrice())
                .credits(packageRequest.getCredits())
                .validityDays(packageRequest.getValidityDays())
                .country(country)
                .active(true)
                .build();

        final PackageEntity savedPackageEntity = this.packageRepository.saveAndFlush(packageEntity);

        final Long countryId = savedPackageEntity.getCountry().getId();
        final String countryCode = savedPackageEntity.getCountry().getCode();

        this.cacheService.evictCachedValue("packagesByCountry", countryId.toString());

        List<PackageDTO> updatedPackages = this.packageRepository.findByCountryIdAndActiveTrue(countryId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        this.cacheService.putCachedValue("packagesByCountry", countryId.toString(), updatedPackages);

        List<PackageDTO> updatedPackagesByCode = this.packageRepository.findActivePackagesByCountryCode(countryCode)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        this.cacheService.putCachedValue("packagesByCountryCode", countryCode, updatedPackagesByCode);

        List<CountryWithPackagesDTO> updatedCountryPackages = this.getAllActivePackagesByCountry(true);
        this.cacheService.putCachedValue("activePackagesByCountry", "activePackagesByCountry", updatedCountryPackages);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackageDTO> getActivePackagesByCountry(final Long countryId) {

        @SuppressWarnings("unchecked")
        List<PackageDTO> cachedPackages = (List<PackageDTO>) this.cacheService.getCachedValue("packagesByCountry", countryId.toString());

        if (cachedPackages != null) {
            return cachedPackages;
        }

        this.countryRepository.findById(countryId)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with id: " + countryId));

        final List<PackageDTO> packages = this.packageRepository.findByCountryIdAndActiveTrue(countryId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        this.cacheService.putCachedValue("packagesByCountry", countryId.toString(), packages);

        return packages;
    }

    @Override
    public List<PackageDTO> getActivePackagesByCountryCode(final String countryCode) {

        @SuppressWarnings("unchecked")
        List<PackageDTO> cachedPackages = (List<PackageDTO>) this.cacheService.getCachedValue("packagesByCountryCode", countryCode);
        if (cachedPackages != null) {
            return cachedPackages;
        }

        this.countryRepository.findByCode(countryCode)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with code: " + countryCode));

        List<PackageDTO> packages = this.packageRepository.findActivePackagesByCountryCode(countryCode)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        this.cacheService.putCachedValue("packagesByCountryCode", countryCode, packages);
        return packages;
    }

    @Transactional(readOnly = true)
    public List<CountryWithPackagesDTO> getAllActivePackagesByCountry(final boolean isCreated) {
        String cacheKey = "activePackagesByCountry";
        if (!isCreated) {

            @SuppressWarnings("unchecked")
            List<CountryWithPackagesDTO> cachedData = (List<CountryWithPackagesDTO>) cacheService.getCachedValue("activePackagesByCountry", cacheKey);
            if (cachedData != null) {
                return cachedData;
            }
        }

        List<Country> activeCountries = countryRepository.findByActiveTrue();

        List<CountryWithPackagesDTO> result = activeCountries.stream()
                .map(country -> {
                    List<PackageDTO> packages = packageRepository.findByCountryIdAndActiveTrue(country.getId())
                            .stream()
                            .map(this::convertToDTO)
                            .collect(Collectors.toList());

                    return new CountryWithPackagesDTO(
                            country.getId(),
                            country.getName(),
                            country.getCode(),
                            packages
                    );
                })
                .collect(Collectors.toList());


        cacheService.putCachedValue("activePackagesByCountry", cacheKey, result);
        return result;
    }

    private PackageDTO convertToDTO(PackageEntity packageEntity) {
        return new PackageDTO(
                packageEntity.getId(),
                packageEntity.getName(),
                packageEntity.getDescription(),
                packageEntity.getPrice(),
                packageEntity.getCredits(),
                packageEntity.getValidityDays(),
                new CountryDTO(
                        packageEntity.getCountry().getId(),
                        packageEntity.getCountry().getName(),
                        packageEntity.getCountry().getCode()
                )
        );
    }

    @Override
    public List<UserPurchasedPackageDTO> getUserPurchasedPackages(final String authHeader) {
        final UserDto userDto = this.userUtil.getCurrentUserDto(authHeader);
        final String cacheKey = "userPurchasedPackages:" + userDto.getId();

        @SuppressWarnings("unchecked")
        List<UserPurchasedPackageDTO> cachedData = (List<UserPurchasedPackageDTO>) cacheService.getCachedValue("userPurchasedPackages", cacheKey);
        if (cachedData != null) {
            return cachedData;
        }

        final List<UserPackage> packages = this.userPackageRepository.findByUserId(userDto.getId());

        final List<UserPurchasedPackageDTO> result = packages.stream().map(pkg -> {
            final LocalDateTime expiryDate = pkg.getPurchaseDate().plusDays(pkg.getPackageEntity().getValidityDays());
            final boolean isExpired = LocalDateTime.now().isAfter(expiryDate);

            final String status = isExpired
                    ? UserPackageStatus.EXPIRED.getCode()
                    : UserPackageStatus.fromInt(pkg.getStatus()).getCode();

            return new UserPurchasedPackageDTO(
                    pkg.getId(),
                    pkg.getPackageEntity().getName(),
                    pkg.getPackageEntity().getPrice(),
                    pkg.getPackageEntity().getCredits(),
                    pkg.getRemainingCredits(),
                    status,
                    pkg.getPurchaseDate().toString(),
                    expiryDate.toString()
            );
        }).collect(Collectors.toList());

        this.cacheService.putCachedValue("userPurchasedPackages", cacheKey, result);

        return result;
    }
}

package com.vinn.bookingSystem.features.packages.controller;

import com.vinn.bookingSystem.config.request.RequestUtils;
import com.vinn.bookingSystem.config.response.dto.ApiResponse;
import com.vinn.bookingSystem.config.response.utils.ResponseUtils;
import com.vinn.bookingSystem.features.packages.dto.CountryWithPackagesDTO;
import com.vinn.bookingSystem.features.packages.dto.PackageDTO;
import com.vinn.bookingSystem.features.packages.service.PackageService;
import com.vinn.bookingSystem.features.userpackages.dto.UserPurchasedPackageDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Collections;
import java.util.List;

@Tag(name = "Package Module", description = "Endpoints for retrieving class packages and user purchases")
@RestController
@RequestMapping("/booking-system/api/v1/packages")
@AllArgsConstructor
@Slf4j
public class PackageController {

    private final PackageService packageService;

    @Operation(
            summary = "Retrieve active packages by country ID",
            description = "Fetches a list of active packages for the specified country ID.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of active packages",
                            content = @Content(schema = @Schema(implementation = PackageDTO.class)))
            }
    )
    @GetMapping("/active/country/{countryId}")
    public ResponseEntity<ApiResponse> retrieveActivePackagesByCountry(
            @Parameter(description = "Numeric ID of the country", example = "1")
            @PathVariable final Long countryId,
            final HttpServletRequest request
    ) {
        log.info("Retrieving active packages for countryId: {}", countryId);

        final double requestStartTime = RequestUtils.extractRequestStartTime(request);

        final List<PackageDTO> activePackages = this.packageService.getActivePackagesByCountry(countryId);

        log.info("Retrieved active packages for countryId {}: {}", countryId,
                (activePackages != null) ? activePackages.size() : 0);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(activePackages != null ? activePackages : Collections.emptyList())
                .message("Active packages retrieved successfully")
                .build();

        return ResponseUtils.buildResponse(request, successResponse, requestStartTime);
    }

    @Operation(
            summary = "Retrieve active packages by country code",
            description = "Fetches a list of active packages for the specified country ISO code.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of active packages",
                            content = @Content(schema = @Schema(implementation = PackageDTO.class)))
            }
    )
    @GetMapping("/active/code/{countryCode}")
    public ResponseEntity<ApiResponse> retrieveActivePackagesByCountryCode(
            @Parameter(description = "ISO country code (e.g., MM, SG)", example = "MM")
            @PathVariable final String countryCode,
            final HttpServletRequest request
    ) {
        log.info("Retrieving active packages for countryCode: {}", countryCode);

        final double requestStartTime = RequestUtils.extractRequestStartTime(request);

        final List<PackageDTO> activePackages = this.packageService.getActivePackagesByCountryCode(countryCode);

        log.info("Retrieved active packages for countryCode {}: {}", countryCode,
                (activePackages != null) ? activePackages.size() : 0);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(activePackages != null ? activePackages : Collections.emptyList())
                .message("Active packages retrieved successfully")
                .build();

        return ResponseUtils.buildResponse(request, successResponse, requestStartTime);
    }

    @Operation(
            summary = "Retrieve all active packages grouped by country",
            description = "Fetches a list of all active packages grouped by country.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of active packages by country",
                            content = @Content(schema = @Schema(implementation = CountryWithPackagesDTO.class)))
            }
    )
    @GetMapping("/active/countries")
    public ResponseEntity<ApiResponse> retrieveAllActivePackagesByCountry(
            final HttpServletRequest request
    ) {
        log.info("Retrieving all active packages for all countries");

        final double requestStartTime = RequestUtils.extractRequestStartTime(request);

        final List<CountryWithPackagesDTO> activePackagesByCountry = this.packageService.getAllActivePackagesByCountry(false);

        log.info("Retrieved active packages for all countries: {}",
                (activePackagesByCountry != null) ? activePackagesByCountry.size() : 0);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(activePackagesByCountry != null ? activePackagesByCountry : Collections.emptyList())
                .message("Active packages for all countries retrieved successfully")
                .build();

        return ResponseUtils.buildResponse(request, successResponse, requestStartTime);
    }

    @Operation(
            summary = "Retrieve user's purchased packages",
            description = "Fetches the list of packages a user has purchased using the Authorization header.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of purchased packages",
                            content = @Content(schema = @Schema(implementation = UserPurchasedPackageDTO.class)))
            }
    )
    @GetMapping("/user-purchased")
    public ResponseEntity<ApiResponse> retrieveUserPurchasedPackages(
            @Parameter(description = "Authorization bearer token")
            final HttpServletRequest request,
            @RequestHeader("Authorization") final String authHeader
    ) {
        log.info("Retrieving user purchased packages");

        final double requestStartTime = RequestUtils.extractRequestStartTime(request);

        final String maskedAuthHeader = authHeader != null ? authHeader.substring(0, Math.min(10, authHeader.length())) + "***" : "N/A";
        log.info("Processing blog for Authorization: {}", maskedAuthHeader);

        final List<UserPurchasedPackageDTO> purchasedPackages = this.packageService.getUserPurchasedPackages(authHeader);

        log.info("Retrieved {} purchased packages",
                (purchasedPackages != null) ? purchasedPackages.size() : 0);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(purchasedPackages != null ? purchasedPackages : Collections.emptyList())
                .message("User purchased packages retrieved successfully")
                .build();

        return ResponseUtils.buildResponse(request, successResponse, requestStartTime);
    }
}

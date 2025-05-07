package com.vinn.bookingSystem.features.booking.controller;

import com.vinn.bookingSystem.config.request.RequestUtils;
import com.vinn.bookingSystem.config.response.dto.ApiResponse;
import com.vinn.bookingSystem.config.response.utils.ResponseUtils;
import com.vinn.bookingSystem.features.booking.dto.BookClassRequest;
import com.vinn.bookingSystem.features.booking.entity.BookingResult;
import com.vinn.bookingSystem.features.booking.service.BookingService;
import com.vinn.bookingSystem.features.classschedule.dto.ClassScheduleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@Tag(name = "Booking Module", description = "Endpoints for booking operations")
@RequestMapping("/booking-system/api/v1/bookings")
@AllArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @Operation(
            summary = "Retrieve available class schedules by country code",
            description = "Returns a list of available class schedules for the given country.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "List of class schedules",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClassScheduleDTO.class))
                    )
            }
    )
    @GetMapping("/available/{countryCode}")
    public ResponseEntity<ApiResponse> retrieveAvailableClassSchedules(
            @Parameter(description = "ISO country code", example = "MM")
            @PathVariable final String countryCode,
            final HttpServletRequest request
    ) {
        log.info("Retrieving available class schedules for countryCode: {}", countryCode);

        final double requestStartTime = RequestUtils.extractRequestStartTime(request);

        final List<ClassScheduleDTO> availableSchedules = this.bookingService.getClassSchedulesByCountry(countryCode);

        log.info("Retrieved available schedules for countryCode {}: {}", countryCode,
                (availableSchedules != null) ? availableSchedules.size() : 0);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(availableSchedules != null ? availableSchedules : Collections.emptyList())
                .message("Available class schedules retrieved successfully")
                .build();

        return ResponseUtils.buildResponse(request, successResponse, requestStartTime);
    }

    @Operation(
            summary = "Book a class by schedule ID",
            description = "Books a class for a user with the given schedule ID.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking result")
            }
    )
    @PostMapping("/book/{scheduleId}")
    public ResponseEntity<ApiResponse> bookClass(
            @Parameter(description = "ID of the class schedule")
            @PathVariable final Long scheduleId,
            @Validated @RequestBody final BookClassRequest bookRequest,
            final HttpServletRequest request
    ) {
        log.info("Booking class for scheduleId: {}", scheduleId);

        final double requestStartTime = RequestUtils.extractRequestStartTime(request);

        final BookingResult result = this.bookingService.bookClass(bookRequest.getUserId(), scheduleId);

        ApiResponse response = ApiResponse.builder()
                .success(result == BookingResult.SUCCESS ? 1 : 0)
                .code(HttpStatus.OK.value())
                .message(result.getMessage())
                .build();
        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }

    @Operation(
            summary = "Cancel a booking",
            description = "Cancels a booking by booking ID.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cancel booking result")
            }
    )
    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<ApiResponse> cancelBooking(
            @Parameter(description = "ID of the booking to cancel")
            @PathVariable final Long bookingId,
            final HttpServletRequest request
    ) {
        final double requestStartTime = RequestUtils.extractRequestStartTime(request);

        boolean isCancelled = this.bookingService.cancelBooking(bookingId);

        ApiResponse response = ApiResponse.builder()
                .success(isCancelled ? 1 : 0)
                .code(isCancelled ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
                .message(isCancelled ? "Booking cancelled successfully" : "Failed to cancel the booking.")
                .build();

        return ResponseUtils.buildResponse(request, response, requestStartTime);
    }
}

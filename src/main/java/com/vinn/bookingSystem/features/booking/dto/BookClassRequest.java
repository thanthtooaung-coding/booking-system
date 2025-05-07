package com.vinn.bookingSystem.features.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookClassRequest {
    @NotNull(message = "userId is required")
    private Long userId;
}

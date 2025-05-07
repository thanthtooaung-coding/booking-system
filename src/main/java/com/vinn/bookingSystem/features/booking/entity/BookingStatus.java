package com.vinn.bookingSystem.features.booking.entity;

import java.util.Arrays;

public enum BookingStatus {
    INVALID(0, "Invalid"),
    BOOKED(1, "Booked"),
    CHECKED_IN(2, "Checked-In"),
    COMPLETED(3, "Completed"),
    CANCELLED(4, "Cancelled"),
    NO_SHOW(5, "No-Show");

    private final Integer value;
    private final String code;

    BookingStatus(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public static BookingStatus fromInt(Integer value) {
        if (value == null) return INVALID;

        return Arrays.stream(BookingStatus.values())
                .filter(status -> status.getValue().equals(value))
                .findFirst()
                .orElse(INVALID);
    }

    public boolean isInvalid() {
        return this.value.equals(INVALID.getValue());
    }

    public static boolean isValidValue(Integer value) {
        return value != null && !fromInt(value).isInvalid();
    }

    public boolean isBooked() {
        return this == BOOKED;
    }

    public boolean isCheckedIn() {
        return this == CHECKED_IN;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }

    public boolean isNoShow() {
        return this == NO_SHOW;
    }
}

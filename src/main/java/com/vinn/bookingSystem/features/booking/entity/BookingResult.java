package com.vinn.bookingSystem.features.booking.entity;

public enum BookingResult {
    SUCCESS(1, "Class booked successfully"),
    FULL(2, "Class is full, user added to waitlist"),
    NO_PACKAGE(3, "User has no valid package with credits"),
    OVERLAPPING(4, "Booking overlaps with an existing booking"),
    COUNTRY_MISMATCH(5, "User package country does not match class schedule"),
    ERROR(99, "Internal error occurred during booking");

    private final int code;
    private final String message;

    BookingResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

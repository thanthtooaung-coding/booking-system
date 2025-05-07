package com.vinn.bookingSystem.features.waitlist.entity;

import java.util.Arrays;

public enum WaitlistStatus {
    INVALID(0, "Invalid"),
    WAITING(1, "Waiting"),
    BOOKED(2, "Booked"),
    EXPIRED(3, "Expired"),
    CANCELLED(4, "Cancelled");

    private final Integer value;
    private final String code;

    WaitlistStatus(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public static WaitlistStatus fromInt(Integer value) {
        if (value == null) return INVALID;

        return Arrays.stream(WaitlistStatus.values())
                .filter(status -> status.getValue().equals(value))
                .findFirst()
                .orElse(INVALID);
    }

    public boolean isInvalid() {
        return this == INVALID;
    }

    public boolean isWaiting() {
        return this == WAITING;
    }

    public boolean isBooked() {
        return this == BOOKED;
    }

    public boolean isExpired() {
        return this == EXPIRED;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }

    public static boolean isValidValue(Integer value) {
        return value != null && !fromInt(value).isInvalid();
    }
}

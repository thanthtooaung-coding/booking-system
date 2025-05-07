package com.vinn.bookingSystem.features.userpackages.entity;

import java.util.Arrays;

public enum UserPackageStatus {
    INVALID(0, "Invalid"),
    ACTIVE(1, "Active"),
    EXPIRED(2, "Expired"),
    CANCELLED(3, "Cancelled");

    private final Integer value;
    private final String code;

    UserPackageStatus(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() { return value; }

    public String getCode() { return code; }

    public static UserPackageStatus fromInt(Integer value) {
        if (value == null) return INVALID;

        return Arrays.stream(UserPackageStatus.values())
                .filter(status -> status.getValue().equals(value))
                .findFirst()
                .orElse(INVALID);
    }

    public boolean isInvalid() { return this == INVALID; }

    public boolean isExpired() { return this == EXPIRED; }

    public static boolean isValidValue(Integer value) {
        return value != null && !fromInt(value).isInvalid();
    }
}

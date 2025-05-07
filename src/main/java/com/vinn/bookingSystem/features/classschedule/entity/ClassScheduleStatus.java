package com.vinn.bookingSystem.features.classschedule.entity;

import java.util.Arrays;

public enum ClassScheduleStatus {
    INVALID(0, "Invalid"),
    SCHEDULED(1, "Scheduled"),
    IN_PROGRESS(2, "In-Progress"),
    COMPLETED(3, "Completed"),
    CANCELLED(4, "Cancelled");

    private final Integer value;
    private final String code;

    ClassScheduleStatus(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public static ClassScheduleStatus fromInt(Integer value) {
        if (value == null) return INVALID;

        return Arrays.stream(ClassScheduleStatus.values())
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

    public boolean isScheduled() {
        return this == SCHEDULED;
    }

    public boolean isInProgress() {
        return this == IN_PROGRESS;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }
}

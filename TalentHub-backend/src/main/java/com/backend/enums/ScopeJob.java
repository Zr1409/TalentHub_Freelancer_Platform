package com.backend.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ScopeJob {
    SMALL("Nhỏ"),
    MEDIUM("Vừa"),
    LARGE("Lớn");

    private final String displayName;

    // Constructor
    ScopeJob(String displayName) {
        this.displayName = displayName;
    }

    // Trả về displayName khi chuyển Enum sang JSON
    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    // Chuyển JSON thành Enum
    @JsonCreator
    public static StatusJob fromString(String status) {
        for (StatusJob jobStatus : StatusJob.values()) {
            if (jobStatus.getDisplayName().equalsIgnoreCase(status) || jobStatus.name().equalsIgnoreCase(status)) {
                return jobStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}

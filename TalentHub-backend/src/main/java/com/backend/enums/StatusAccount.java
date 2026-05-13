package com.backend.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusAccount {
    ACTIVE("Hoạt động"),
    //INACTIVE("Không hoạt động"),
    //BANNED("Khoá");
    LOCKED("Khoá");

    private final String displayName;

    StatusAccount(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
    // Chuyển JSON thành Enum
    @JsonCreator
    public static StatusAccount fromString(String status) {
        for (StatusAccount accountStatus : StatusAccount.values()) {
            if (accountStatus.getDisplayName().equalsIgnoreCase(status) || accountStatus.name().equalsIgnoreCase(status)) {
                return accountStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }

}

package com.backend.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActivityType {
    DEPOSIT("Nạp tiền"),
    WITHDRAW("Rút tiền");

    private final String displayName;

    // Constructor
    ActivityType(String displayName) {
        this.displayName = displayName;
    }

    // Trả về displayName khi chuyển Enum sang JSON
    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    // Chuyển JSON thành Enum
    @JsonCreator
    public static ActivityType fromString(String value) {
        for (ActivityType type : ActivityType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy trạng thái giao dịch hợp lệ: " + value);
    }

}

package com.backend.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProgressJob {
    InProgress("Đang thực hiện"),
    Completed("Hoàn thành"),
    Pending("Chờ xử lý"),
    Cancle("Hủy bỏ");

    private final String displayName;

    // Constructor với displayName
    ProgressJob(String displayName) {
        this.displayName = displayName;
    }

    // Getter cho displayName
    @JsonValue
    public String getDisplayName() {
        return displayName;
    }


    public static ProgressJob fromString(String status) {
        for (ProgressJob jobStatus : ProgressJob.values()) {
            if (jobStatus.getDisplayName().equalsIgnoreCase(status)) {
                return jobStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }

}

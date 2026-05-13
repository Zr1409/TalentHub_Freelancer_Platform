package com.backend.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusFreelancerJob {
    Applied("Applied"),       // Applied
    Cancelled("Cancelled"),    // Cancelled
//    InProgress("In Progress"), // In Progress
//    Completed("Completed"),    // Completed
    Approved("Approved"),
    Rejected("Rejected"),
    Viewed("Viewed");          // Viewed

    private final String displayName;

    // Constructor với displayName
    StatusFreelancerJob(String displayName) {
        this.displayName = displayName;
    }

    // Getter cho displayName
    @JsonValue
        public String getDisplayName() {
        return displayName;
    }

    // Phương thức tìm enum từ displayName
    public static StatusFreelancerJob fromDisplayName(String displayName) {
        for (StatusFreelancerJob status : StatusFreelancerJob.values()) {
            if (status.getDisplayName().equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy trạng thái phù hợp: " + displayName);
    }
}

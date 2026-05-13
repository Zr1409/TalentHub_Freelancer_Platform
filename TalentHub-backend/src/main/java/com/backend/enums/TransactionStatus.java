package com.backend.enums;

public enum TransactionStatus {
    PENDING("Chờ xử lý"),
    SUCCESS("Thành công"),
    FAILED("Thất bại");

    private final String displayName;

    // Constructor with displayName
    TransactionStatus(String displayName) {
        this.displayName = displayName;
    }

    // Getter for displayName
    public String getDisplayName() {
        return displayName;
    }

    public static TransactionStatus fromString(String status) {
        for (TransactionStatus transactionStatus : TransactionStatus.values()) {
            if (transactionStatus.getDisplayName().equalsIgnoreCase(status)) {
                return transactionStatus;
            }
        }
        throw new IllegalArgumentException("Unknown transaction status: " + status);
    }
}

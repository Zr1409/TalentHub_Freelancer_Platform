package com.backend.enums;

public enum TypePayment {
    HOURLY("Theo giờ"),
    FULL("Theo dự án");

    private final String displayName;

    // Constructor với displayName
    TypePayment(String displayName) {
        this.displayName = displayName;
    }

    // Getter cho displayName
    public String getDisplayName() {
        return displayName;
    }

    public static TypePayment fromString(String paymentType) {
        for (TypePayment type : TypePayment.values()) {
            if (type.getDisplayName().equalsIgnoreCase(paymentType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown payment type: " + paymentType);
    }

}

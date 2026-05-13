package com.backend.enums;

public enum TypePackage {
    NORMAL("Gói thường", 1),
    SILVER("Gói bạc", 2),
    GOLD("Gói vàng", 3),
    DIAMOND("Gói kim cương", 4);

    private final String displayName;
    private final int order;

    // Constructor với displayName
    TypePackage(String displayName, int order) {
        this.displayName = displayName;
        this.order = order;
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

    public int getOrder() {
        return order;
    }
}

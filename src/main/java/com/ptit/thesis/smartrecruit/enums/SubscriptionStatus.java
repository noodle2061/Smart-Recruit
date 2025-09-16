package com.ptit.thesis.smartrecruit.enums;

public enum SubscriptionStatus {
    ACTIVE("Active"),
    EXPIRED("Expired"),
    CANCELLED("Cancelled");

    private final String displayValue;

    SubscriptionStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    @Override
    public String toString() {
        return displayValue;
    }
}
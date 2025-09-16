package com.ptit.thesis.smartrecruit.enums;

public enum InvoiceStatus {
    PAID("Paid"),
    PENDING("Pending"),
    FAILED("Failed");

    private final String displayValue;

    InvoiceStatus(String displayValue) {
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
package com.ptit.thesis.smartrecruit.enums;

public enum MaritalStatus {
    SINGLE("Single"),
    MARRIED("Married"),
    OTHER("Other");

    private final String displayValue;

    MaritalStatus(String displayValue) {
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
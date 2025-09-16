package com.ptit.thesis.smartrecruit.enums;

public enum JobStatus {
    ACTIVE("Active"),
    EXPIRED("Expired"),
    FILLED("Filled"),
    DRAFT("Draft");

    private final String displayValue;

    JobStatus(String displayValue) {
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
package com.ptit.thesis.smartrecruit.enums;

public enum JobAlertFrequency {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly");

    private final String displayValue;

    JobAlertFrequency(String displayValue) {
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
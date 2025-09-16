package com.ptit.thesis.smartrecruit.enums;

public enum SalaryType {
    MONTHLY("Monthly"),
    YEARLY("Yearly"),
    HOURLY("Hourly");

    private final String displayValue;

    SalaryType(String displayValue) {
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
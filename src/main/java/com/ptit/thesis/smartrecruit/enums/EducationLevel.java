package com.ptit.thesis.smartrecruit.enums;

public enum EducationLevel {
    HIGH_SCHOOL("HIGH_SCHOOL"),
    INTERMEDIATE("INTERMEDIATE"),
    BACHELOR_DEGREE("BACHELOR_DEGREE"),
    MASTER_DEGREE("MASTER_DEGREE"),
    DOCTORATE("DOCTORATE");

    private final String displayValue;

    EducationLevel(String displayValue) {
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
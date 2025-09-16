package com.ptit.thesis.smartrecruit.enums;

public enum EducationLevel {
    HIGH_SCHOOL("High School"),
    INTERMEDIATE("Intermediate"),
    BACHELOR_DEGREE("Bachelor Degree"),
    MASTER_DEGREE("Master Degree"),
    DOCTORATE("Doctorate");

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
package com.ptit.thesis.smartrecruit.enums;

public enum JobType {
    FULL_TIME("FULL_TIME"),
    PART_TIME("PART_TIME"),
    INTERNSHIP("INTERNSHIP"),
    REMOTE("REMOTE"),
    TEMPORARY("TEMPORARY"),
    CONTRACT_BASE("CONTRACT_BASE");

    private final String displayValue;

    JobType(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    @Override
    public String toString() {
        return name();
    }
}
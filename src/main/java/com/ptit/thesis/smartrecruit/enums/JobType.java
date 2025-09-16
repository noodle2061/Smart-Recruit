package com.ptit.thesis.smartrecruit.enums;

public enum JobType {
    FULL_TIME("Full Time"),
    PART_TIME("Part Time"),
    INTERNSHIP("Internship"),
    REMOTE("Remote"),
    TEMPORARY("Temporary"),
    CONTRACT_BASE("Contract Base");

    private final String displayValue;

    JobType(String displayValue) {
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
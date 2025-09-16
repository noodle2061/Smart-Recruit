package com.ptit.thesis.smartrecruit.enums;

public enum JobApplicationStatus {
    SUBMITTED("Submitted"),
    VIEWED("Viewed"),
    SHORTLISTED("Shortlisted"),
    INTERVIEWING("Interviewing"),
    HIRED("Hired"),
    REJECTED("Rejected");

    private final String displayValue;

    JobApplicationStatus(String displayValue) {
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
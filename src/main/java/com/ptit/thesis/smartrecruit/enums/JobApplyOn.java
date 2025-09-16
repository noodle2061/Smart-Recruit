package com.ptit.thesis.smartrecruit.enums;

public enum JobApplyOn {
    ON_PLATFORM("On Jobpilot"),
    EXTERNAL_LINK("External Platform"),
    BY_EMAIL("On Your Email");

    private final String displayValue;

    JobApplyOn(String displayValue) {
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
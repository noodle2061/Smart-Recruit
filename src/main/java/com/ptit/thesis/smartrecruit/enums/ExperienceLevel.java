package com.ptit.thesis.smartrecruit.enums;

public enum ExperienceLevel {
    FRESHER("FRESHER"),
    ONE_TO_TWO_YEARS("ONE_TO_TWO_YEARS"),
    TWO_TO_FOUR_YEARS("TWO_TO_FOUR_YEARS"),
    FOUR_TO_SIX_YEARS("FOUR_TO_SIX_YEARS"),
    SIX_TO_TEN_YEARS("SIX_TO_TEN_YEARS"),
    TEN_PLUS_YEARS("TEN_PLUS_YEARS");

    private final String displayValue;

    ExperienceLevel(String displayValue) {
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
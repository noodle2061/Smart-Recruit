package com.ptit.thesis.smartrecruit.enums;

public enum ExperienceLevel {
    FRESHER("Fresher"),
    ONE_TO_TWO_YEARS("1-2 Years"),
    TWO_TO_FOUR_YEARS("2-4 Years"),
    FOUR_TO_SIX_YEARS("4-6 Years"),
    SIX_TO_TEN_YEARS("6-10 Years"),
    TEN_PLUS_YEARS("10+ Years");

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
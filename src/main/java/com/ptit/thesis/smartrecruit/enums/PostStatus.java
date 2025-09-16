package com.ptit.thesis.smartrecruit.enums;

public enum PostStatus {
    DRAFT("Draft"),
    PUBLISHED("Published");

    private final String displayValue;

    PostStatus(String displayValue) {
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
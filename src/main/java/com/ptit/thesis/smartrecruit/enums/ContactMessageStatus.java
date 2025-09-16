package com.ptit.thesis.smartrecruit.enums;

public enum ContactMessageStatus {
    NEW("New"),
    READ("Read"),
    REPLIED("Replied");

    private final String displayValue;

    ContactMessageStatus(String displayValue) {
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
package com.ptit.thesis.smartrecruit.enums;

public enum OrganizationType {
    GOVERNMENT("Government"),
    SEMI_GOVERNMENT("Semi-Government"),
    NGO("NGO"),
    PRIVATE_COMPANY("Private Company"),
    STARTUP("Startup"),
    MULTINATIONAL("Multinational");

    private final String displayValue;

    OrganizationType(String displayValue) {
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
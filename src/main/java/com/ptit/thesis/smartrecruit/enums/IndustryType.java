package com.ptit.thesis.smartrecruit.enums;

public enum IndustryType {
    IT("IT"),
    MARKETING("Marketing"),
    FINANCE("Finance"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education"),
    RETAIL("Retail"),
    MANUFACTURING("Manufacturing"),
    CONSTRUCTION("Construction"),
    TELECOMMUNICATIONS("Telecommunications"),
    LOGISTICS("Logistics"),
    HOSPITALITY("Hospitality"),
    ENERGY("Energy");

    private final String displayValue;

    IndustryType(String displayValue) {
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
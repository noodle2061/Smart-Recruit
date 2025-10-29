package com.ptit.thesis.smartrecruit.enums;

public enum BlogStatus {
    FULLTIME("FullTime"),
    PARTTIME("PartTime");

    private final String displayValue;

    BlogStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return this.displayValue;
    }

    @Override
    public String toString() {
        return this.displayValue;        
    }
}

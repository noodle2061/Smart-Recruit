package com.ptit.thesis.smartrecruit.enums;

public enum CompanyTeamSize {
    ONE_TO_FIFTY("1-50"),
    FIFTY_ONE_TO_HUNDRED("51-100"),
    HUNDRED_ONE_TO_TWO_FIFTY("101-250"),
    TWO_FIFTY_ONE_TO_FIVE_HUNDRED("251-500"),
    FIVE_HUNDRED_ONE_TO_THOUSAND("501-1,000"),
    THOUSAND_PLUS("1,000+");

    private final String displayValue;

    CompanyTeamSize(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
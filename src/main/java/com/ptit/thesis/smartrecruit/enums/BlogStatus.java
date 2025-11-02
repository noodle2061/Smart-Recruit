package com.ptit.thesis.smartrecruit.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.Hidden;

public enum BlogStatus {
    DRAFT("DRAFT"),
    PUBLISHED("PUBLISHED"),
    REQUESTED("REQUESTED");

    private final String displayValue;

    BlogStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    @Hidden
    public String getDisplayValue() {
        return this.displayValue;
    }

}

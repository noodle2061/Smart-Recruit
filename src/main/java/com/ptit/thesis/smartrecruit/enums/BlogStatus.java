package com.ptit.thesis.smartrecruit.enums;

import lombok.Getter;

@Getter
public enum BlogStatus {
    DRAFT("DRAFT"),
    PUBLISHED("PUBLISHED"),
    REQUESTED("REQUESTED");

    private final String displayValue;

    BlogStatus(String displayValue) {
        this.displayValue = displayValue;
    }
}

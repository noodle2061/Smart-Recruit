package com.ptit.thesis.smartrecruit.enums;

public enum JobApplicationStatus {
    SUBMITTED("Đã nộp"),
    PROCESSING("Đang xử lý"),
    SENT_EMAIL("Đã gửi mail"),
    REJECTED("Đã từ chối");

    private final String displayValue;

    JobApplicationStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    @Override
    public String toString() {
        return name();
    }
}
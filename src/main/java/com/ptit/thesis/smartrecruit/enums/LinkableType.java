package com.ptit.thesis.smartrecruit.enums;

public enum LinkableType {
    CANDIDATE_PROFILE("CandidateProfile"),
    COMPANY("Company");

    private final String name;

    LinkableType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
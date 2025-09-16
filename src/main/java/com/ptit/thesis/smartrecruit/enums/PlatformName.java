package com.ptit.thesis.smartrecruit.enums;

public enum PlatformName {
    LINKEDIN("Linked In"),
    X("X"),
    FACEBOOK("Facebook"),
    INSTAGRAM("Instagram"),
    GITHUB("GitHub"),
    GITLAB("GitLab"),
    YOUTUBE("YouTube"),
    PORTFOLIO("Portfolio");

    private final String displayName;

    PlatformName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
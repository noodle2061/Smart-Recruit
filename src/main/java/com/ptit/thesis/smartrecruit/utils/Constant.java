package com.ptit.thesis.smartrecruit.utils;

import java.time.Duration;

public class Constant {
    public static final String CANDIDATE_ROLE = "CANDIDATE";
    public static final String EMPLOYER_ROLE = "EMPLOYER";

    public static final String INTERNATIONAL_PHONE_REGEX = "^\\+(?:[0-9] ?){6,15}[0-9]$";
    public static final String VIETNAM_PHONE_REGEX = "^(\\+84|0)(3[2-9]|5[25689]|7[0|6-9]|8[1-9]|9[0-9])\\d{7}$";

    public static final Integer APPLY_RATE_LIMIT_MUNITE = 10;
    public static final Duration APPLY_RATE_LIMIT_DURATION = Duration.ofMinutes(APPLY_RATE_LIMIT_MUNITE);
    public static final String APPLY_LIMIT_PREFIX = "apply_limit:";
}

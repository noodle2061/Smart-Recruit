package com.ptit.thesis.smartrecruit.service;

import java.time.Duration;

public interface RedisService {
    public void setValue(String key, Object value, Duration timeout);
    public Object getValue(String key);
    public void deleteValue(String key);
    public boolean hasKey(String key);
    public boolean isApplyRateLimit(Long applicationId);
    public void setApplyRateLimit(Long applicationId);
}

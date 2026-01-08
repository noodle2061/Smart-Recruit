package com.ptit.thesis.smartrecruit.service.impl;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.service.RedisService;
import com.ptit.thesis.smartrecruit.utils.Constant;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setValue(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    @Override
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean isApplyRateLimit(Long applicationId) {
        String key = Constant.APPLY_LIMIT_PREFIX + ":" + applicationId;
        return hasKey(key);
    }

    @Override
    public void setApplyRateLimit(Long applicationId) {
        String key = Constant.APPLY_LIMIT_PREFIX + ":" + applicationId;
        setValue(key, 1, Constant.APPLY_RATE_LIMIT_DURATION);
    }

}

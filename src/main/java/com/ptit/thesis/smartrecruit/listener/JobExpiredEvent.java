package com.ptit.thesis.smartrecruit.listener;

import com.ptit.thesis.smartrecruit.entity.Job;

public record JobExpiredEvent(Job job) {}

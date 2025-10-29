package com.ptit.thesis.smartrecruit.service.impl;

import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.service.BlogService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class BlogServiceImpl implements BlogService {

}

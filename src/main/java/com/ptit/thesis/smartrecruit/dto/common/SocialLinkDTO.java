package com.ptit.thesis.smartrecruit.dto.common;

import com.ptit.thesis.smartrecruit.enums.PlatformName;

import lombok.Data;

@Data
public class SocialLinkDTO {
    PlatformName platformName;
    String url;
}

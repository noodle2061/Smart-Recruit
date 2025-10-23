package com.ptit.thesis.smartrecruit.dto.common;

import com.ptit.thesis.smartrecruit.enums.PlatformName;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocialLinkDTO {
    PlatformName platformName;
    String url;
}

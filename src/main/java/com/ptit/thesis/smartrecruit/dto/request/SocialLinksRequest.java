package com.ptit.thesis.smartrecruit.dto.request;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocialLinksRequest {

    List<SocialLink> socialLinks;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SocialLink {
        String provider;
        String link;
    }
}

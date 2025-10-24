package com.ptit.thesis.smartrecruit.dto.common;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagDTO {
    Long id;
    String name;
}

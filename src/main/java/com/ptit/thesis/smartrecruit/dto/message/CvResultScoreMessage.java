package com.ptit.thesis.smartrecruit.dto.message;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CvResultScoreMessage {
    Long applicationId;
    Double score;
}

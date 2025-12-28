package com.ptit.thesis.smartrecruit.dto.message;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CvResultScoreMessage {
    Long applicationId;
    Boolean isSuccess;
    Integer version;
    MatchDataDTO data;
    String error;
    LocalDateTime timestamp;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class MatchDataDTO {
        private Long applicationId;
        private Double matchScore;
        private BreakdownDTO breakdown;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public class BreakdownDTO {
        private Double hardSkillsScore;
        private Double workExperienceScore;
        private Double responsibilitiesAchievementsScore;
        private Double softSkillsScore;
        private Double educationTrainingScore;
        private Double additionalFactorsScore;
    }
}

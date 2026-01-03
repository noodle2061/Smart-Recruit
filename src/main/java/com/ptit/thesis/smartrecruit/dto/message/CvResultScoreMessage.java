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
    public static class MatchDataDTO {
        private Long applicationId;
        private Double matchScore;
        private BreakdownDTO breakdown;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreakdownDTO {
        private Double hardSkillsScore;
        private Double workExperienceScore;
        private Double responsibilitiesAchievementsScore;
        private Double softSkillsScore;
        private Double educationTrainingScore;
        private Double additionalFactorsScore;

        @Override
        public String toString() {
            return String.format(
                    "{\"hardSkillsScore\":%s,\"workExperienceTimeScore\":%s,\"responsibilitiesAchievementsScore\":%s,\"softSkillsScore\":%s,\"educationTrainingScore\":%s,\"additionalFactorsScore\":%s}",
                    hardSkillsScore,
                    workExperienceScore,
                    responsibilitiesAchievementsScore,
                    softSkillsScore,
                    educationTrainingScore,
                    additionalFactorsScore);
        }
    }
}

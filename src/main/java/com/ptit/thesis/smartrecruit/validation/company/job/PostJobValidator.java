package com.ptit.thesis.smartrecruit.validation.company.job;

import com.ptit.thesis.smartrecruit.dto.request.PostJobRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PostJobValidator implements ConstraintValidator<ValidPostJob, PostJobRequest> {

    @Override
    public boolean isValid(PostJobRequest value, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (value.getMaxSalary().compareTo(value.getMinSalary()) < 0) {
            isValid = false;
            context.buildConstraintViolationWithTemplate("Maximum salary must be greater than or equal to minimum salary")
                            .addPropertyNode("maxSalary")
                            .addConstraintViolation();
        }
        
        return isValid;
    }
}

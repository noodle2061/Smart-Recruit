package com.ptit.thesis.smartrecruit.validation;

import java.util.List;

import jakarta.validation.ConstraintValidator;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {
    private static final List<String> VALID_ROLES = List.of("ADMIN", "EMPLOYER", "CANDIDATE");

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return VALID_ROLES.contains(value.toUpperCase());
    }
    
}

package com.ptit.thesis.smartrecruit.validation.company.profile;

import java.time.Year;

import com.ptit.thesis.smartrecruit.dto.request.CompanyProfileRequest;
import com.ptit.thesis.smartrecruit.utils.Constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompanyProfileValidator implements ConstraintValidator<ValidCompanyProfile, CompanyProfileRequest> {

    @Override
    public boolean isValid(CompanyProfileRequest value, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (!validateFoundedIn(value.getFoundedIn())) {
            isValid = false;
            context.buildConstraintViolationWithTemplate("Founded year cannot be in the future")
                            .addPropertyNode("foundedIn")
                            .addConstraintViolation();
        }
        if (!validatePhone(value.getPhone())) {
            isValid = false;
            context.buildConstraintViolationWithTemplate("Invalid phone number")
                            .addPropertyNode("phone")
                            .addConstraintViolation();
        }
        return isValid;
    }

    public boolean validateFoundedIn(Integer foundedIn) {
        return foundedIn <= Year.now().getValue();
    }

    public boolean validatePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }

        // Regex cho số điện thoại Việt Nam:
        // - Bắt đầu bằng 0, theo sau là 9 chữ số (tổng 10)
        // - Hoặc bắt đầu bằng +84, theo sau là 9 chữ số (tổng 12)
        final String phoneRegex = Constraint.INTERNATIONAL_PHONE_REGEX;

        return phone.matches(phoneRegex);
    }
}

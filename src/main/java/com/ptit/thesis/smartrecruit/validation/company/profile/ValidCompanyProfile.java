package com.ptit.thesis.smartrecruit.validation.company.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Constraint(validatedBy = CompanyProfileValidator.class)
public @interface ValidCompanyProfile {
    String message() default "Invalid company profile's field.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

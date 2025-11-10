package com.ptit.thesis.smartrecruit.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.entity.Job_;
import com.ptit.thesis.smartrecruit.enums.JobStatus;

import jakarta.persistence.criteria.Predicate;

public class EmployerJobSpecification {
    public static Specification<Job> getPredicate(JobStatus status, Company company) {
        return (root, query, cb) -> {

            List<Predicate> predicate = new ArrayList<>();

            if (status != null) {
                predicate.add(cb.equal(root.get(Job_.status), status));
            }

            if (company != null) {
                predicate.add(cb.equal(root.get(Job_.company), company));
            } else {
                throw new IllegalArgumentException("Company cannot be null");
            }
            return cb.and(predicate.toArray(new Predicate[0]));
        };
    }
}

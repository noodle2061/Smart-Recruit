package com.ptit.thesis.smartrecruit.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.dto.response.AppliedJobResponse;
import com.ptit.thesis.smartrecruit.dto.response.JobPageResponse;
import com.ptit.thesis.smartrecruit.entity.QApplication;
import com.ptit.thesis.smartrecruit.entity.QCompany;
import com.ptit.thesis.smartrecruit.entity.QJob;
import com.ptit.thesis.smartrecruit.entity.QJobCategory;
import com.ptit.thesis.smartrecruit.entity.QLocation;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;
import com.ptit.thesis.smartrecruit.enums.JobStatus;
import com.ptit.thesis.smartrecruit.enums.JobType;
import com.ptit.thesis.smartrecruit.utils.StringUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JobRepositoryCustomImpl implements JobRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<JobPageResponse> searchJobsWithFilter(String keyword, 
                                                        String provinceCity, 
                                                        String category, 
                                                        Long minSalary, 
                                                        Long maxSalary, 
                                                        ExperienceLevel experienceLevel,
                                                        List<EducationLevel> educationLevels, 
                                                        List<JobType> jobTypes,
                                                        Pageable pageable) {
        // Q object
        QJob job = QJob.job;
        QCompany company = QCompany.company;
        QLocation location = QLocation.location;
        QJobCategory jobCategory = QJobCategory.jobCategory;

        // xây dựng where bằng BooleanBuilder
        BooleanBuilder predicate = new BooleanBuilder();

        predicate.and(job.status.eq(JobStatus.ACTIVE));

        if (StringUtil.hasText(keyword)) {
            predicate.and(
                job.title.containsIgnoreCase(keyword).or(company.name.containsIgnoreCase(keyword))
                );
        }

        if (StringUtil.hasText(provinceCity)) {
            predicate.and(location.provinceCity.containsIgnoreCase(provinceCity));
        }

        if (StringUtil.hasText(category)) {
            predicate.and(jobCategory.name.containsIgnoreCase(category));
        }

        if (minSalary != null) {
            predicate.and(job.minSalary.goe(BigDecimal.valueOf(minSalary)));
        }

        if (maxSalary != null) {
            predicate.and(job.maxSalary.loe(BigDecimal.valueOf(maxSalary)));
        }

        if (experienceLevel != null) {
            predicate.and(job.experienceLevel.eq(experienceLevel));
        }

        if (educationLevels != null && !educationLevels.isEmpty()) {
            predicate.and(job.educationLevel.in(educationLevels));
        }

        if (jobTypes != null && !jobTypes.isEmpty()) {
            predicate.and(job.type.in(jobTypes));
        }

        var query = jpaQueryFactory
            .select(Projections.constructor(JobPageResponse.class,
                job.id,
                job.slug,
                job.title,
                company.name,
                company.logoUrl,
                location.provinceCity,
                job.type,
                job.minSalary,
                job.maxSalary,
                job.salaryType
            ))
            .from(job)
            .leftJoin(job.company, company)
            .leftJoin(job.location, location)
            .leftJoin(job.jobCategories, jobCategory)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .distinct();

        List<JobPageResponse> results = query.fetch();

        // hoàn thiện các đối tượng của slice
        boolean hasNext = (results.size() > pageable.getPageSize());
        if (hasNext) {
            results = results.subList(0, pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    @Override
    public Page<AppliedJobResponse> getCandidateAppliedJobs(
                                    Pageable pageable, 
                                    String keyword,
                                    JobStatus status) {
        QCompany company = QCompany.company;
        QJob job = QJob.job;
        QLocation location = QLocation.location;
        QApplication application = QApplication.application;

        StringExpression salaryExpression = job.minSalary.stringValue()
            .concat(" - ")
            .concat(job.maxSalary.stringValue())
            .concat("/")
            .concat(job.salaryType.stringValue())
            .as("salary");

        BooleanBuilder predicate = new BooleanBuilder();

        if (status != null) {
            predicate.and(job.status.eq(status));
        }
        
        if (StringUtil.hasText(keyword)) {
            predicate.and(job.title.containsIgnoreCase(keyword)
                            .or(company.name.containsIgnoreCase(keyword))
                            .or(location.provinceCity.containsIgnoreCase(keyword))
            );
        }

        var query = jpaQueryFactory
            .select(Projections.constructor(AppliedJobResponse.class,
                job.id,
                job.slug,
                job.title,
                location.provinceCity,
                company.name,
                company.logoUrl,
                salaryExpression,
                job.type,
                job.status,
                application.createdAt
            ))
            .from(job)
            .leftJoin(job.company, company)
            .leftJoin(job.location, location)
            .leftJoin(job.jobApplications, application)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .distinct();

        List<AppliedJobResponse> results = query.fetch();

        Long total = jpaQueryFactory
            .select(job.id.count())
            .from(job)
            .leftJoin(job.company, company)
            .leftJoin(job.location, location)
            .leftJoin(job.jobApplications, application)
            .where(predicate)
            .fetchOne();
        return new PageImpl<>(results, pageable, total);
    }
    
}

package com.ptit.thesis.smartrecruit.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.dto.request.ApplicationFilterRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApplicationBriefResponse;
import com.ptit.thesis.smartrecruit.entity.Application;
import com.ptit.thesis.smartrecruit.entity.QApplication;
import com.ptit.thesis.smartrecruit.entity.QCandidateProfile;
import com.ptit.thesis.smartrecruit.entity.QResume;
import com.ptit.thesis.smartrecruit.entity.QUser;
import com.ptit.thesis.smartrecruit.service.S3Service;
import com.ptit.thesis.smartrecruit.utils.StringUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ApplicationRepositoryCustomImpl implements ApplicationRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ApplicationBriefResponse> findApplicationsByJobId(Long jobId, ApplicationFilterRequest filter,
            Pageable pageable) {

        QApplication application = QApplication.application;
        QCandidateProfile candidate = QCandidateProfile.candidateProfile;
        QResume resume = QResume.resume;
        QUser user = QUser.user;

        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(application.job.id.eq(jobId));

        if (filter.getAppropriate() != null) {
            predicate.and(application.score.goe(filter.getAppropriate()));
        }

        if (filter.getGender() != null) {
            predicate.and(candidate.gender.eq(filter.getGender()));
        }

        if (StringUtil.hasText(filter.getAgeRange())) {
            try {
                String[] ageRange = filter.getAgeRange().split("-");
                Integer minAge = Integer.parseInt(ageRange[0]);
                Integer maxAge = Integer.parseInt(ageRange[1]);

                LocalDate minDate = LocalDate.now().minusYears(minAge);
                LocalDate maxDate = LocalDate.now().minusYears(maxAge);

                predicate.and(candidate.dateOfBirth.between(minDate, maxDate));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid age range: " + filter.getAgeRange());
            }
        }

        var query = jpaQueryFactory
                .select(Projections.constructor(ApplicationBriefResponse.class,
                        application.id,
                        candidate.id,
                        candidate.fullName,
                        candidate.avatarUrl,
                        candidate.headline,
                        user.email,
                        candidate.experienceLevel,
                        candidate.educationLevel,
                        application.createdAt,
                        resume.storageKey,
                        application.coverLetter,
                        application.score))
                .from(application)
                .join(application.candidate, candidate)
                .join(application.resume, resume)
                .join(application.candidate.user, user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(predicate);

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Application> entityPath = new PathBuilder<>(Application.class, "application");
            query.orderBy(new OrderSpecifier(
                    order.isAscending() ? Order.ASC : Order.DESC,
                    entityPath.get(order.getProperty())));
        }

        List<ApplicationBriefResponse> results = query.fetch();

        Long total = jpaQueryFactory
                .select(application.id.count())
                .from(application)
                .join(application.candidate, candidate)
                .join(application.resume, resume)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }
}

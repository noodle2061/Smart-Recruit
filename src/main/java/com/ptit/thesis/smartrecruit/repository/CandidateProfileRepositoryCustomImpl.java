package com.ptit.thesis.smartrecruit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.dto.response.CandidatePageResponse;
import com.ptit.thesis.smartrecruit.entity.QCandidateCompany;
import com.ptit.thesis.smartrecruit.entity.QCandidateProfile;
import com.ptit.thesis.smartrecruit.entity.QLocation;
import com.ptit.thesis.smartrecruit.entity.QUser;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;
import com.ptit.thesis.smartrecruit.enums.FollowType;
import com.ptit.thesis.smartrecruit.enums.Gender;
import com.ptit.thesis.smartrecruit.utils.StringUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CandidateProfileRepositoryCustomImpl implements CandidateProfileRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CandidatePageResponse> findCandidatesWithFilter(String keyword, String locationProvince, String category,
            ExperienceLevel experienceLevel, List<EducationLevel> educationLevels, Gender gender, Long companyId,
            Pageable pageable) {
        QCandidateProfile candidate = QCandidateProfile.candidateProfile;
        QLocation location = QLocation.location;
        QUser user = QUser.user;
        QCandidateCompany candidateCompany = QCandidateCompany.candidateCompany;

        BooleanExpression isFollowed = JPAExpressions
                .selectOne()
                .from(candidateCompany)
                .where(
                    candidateCompany.company.id.eq(companyId)
                    .and(candidateCompany.candidate.id.eq(candidate.id))
                    .and(candidateCompany.type.eq(FollowType.COMPANY_FOLLOW_CANDIDATE)))
                .exists();

        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtil.hasText(keyword)) {
            predicate.and(candidate.fullName.containsIgnoreCase(keyword)
                    .or(user.email.containsIgnoreCase(keyword))
                    .or(candidate.headline.containsIgnoreCase(keyword)));
        }

        if (StringUtil.hasText(locationProvince)) {
            predicate.and(location.provinceCity.containsIgnoreCase(locationProvince));
        }

        if (experienceLevel != null) {
            predicate.and(candidate.experienceLevel.eq(experienceLevel));
        }

        if (educationLevels != null && !educationLevels.isEmpty()) {
            predicate.and(candidate.educationLevel.in(educationLevels));
        }

        if (gender != null) {
            predicate.and(candidate.gender.eq(gender));
        }

        predicate.and(candidate.isPublic.eq(true));

        var query = jpaQueryFactory
                .select(Projections.constructor(CandidatePageResponse.class,
                    candidate.id,
                    candidate.fullName,
                    candidate.avatarUrl,
                    candidate.headline,
                    candidate.experienceLevel,
                    candidate.educationLevel,
                    Projections.constructor(com.ptit.thesis.smartrecruit.dto.common.LocationDTO.class,
                            location.country,
                            location.provinceCity,
                            location.commune,
                            location.latitude,
                            location.longitude),
                    isFollowed
                ))
                .from(candidate)
                .leftJoin(candidate.location, location)
                .join(candidate.user, user)
                .where(predicate)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());
        
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Object> pathBuilder = new PathBuilder<>(candidate.getType(),candidate.getMetadata());
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            query.orderBy(new OrderSpecifier<>(direction, pathBuilder.get(order.getProperty(), Comparable.class)));
        }

        List<CandidatePageResponse> results = query.fetch();

        Long total = jpaQueryFactory
                .select(candidate.count())
                .from(candidate)
                .join(candidate.user, user)
                .leftJoin(candidate.location, location)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<CandidatePageResponse> findSavedCandidatesForEmployer(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findSavedCandidatesForEmployer'");
    }
    
}

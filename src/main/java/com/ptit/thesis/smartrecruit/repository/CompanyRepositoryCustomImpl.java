package com.ptit.thesis.smartrecruit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.dto.common.LocationDTO;
import com.ptit.thesis.smartrecruit.dto.response.AdminCompanyResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyPageResponse;
import com.ptit.thesis.smartrecruit.entity.QCompany;
import com.ptit.thesis.smartrecruit.entity.QJob;
import com.ptit.thesis.smartrecruit.entity.QLocation;
import com.ptit.thesis.smartrecruit.entity.QUser;
import com.ptit.thesis.smartrecruit.enums.CompanyTeamSize;
import com.ptit.thesis.smartrecruit.enums.IndustryType;
import com.ptit.thesis.smartrecruit.enums.JobStatus;
import com.ptit.thesis.smartrecruit.enums.OrganizationType;
import com.ptit.thesis.smartrecruit.utils.StringUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CompanyRepositoryCustomImpl implements CompanyRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CompanyPageResponse> searchCompany(
                                            String keyword, 
                                            String provinceCity, 
                                            OrganizationType organizationType, 
                                            IndustryType industryType,
                                            CompanyTeamSize teamSize,
                                            Integer foundedIn,
                                            Pageable pageable) {
        QCompany company = QCompany.company;
        QLocation location = QLocation.location;
        QJob job = QJob.job;

        // xây dựng where
        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtil.hasText(keyword)) {
            predicate.and(company.name.containsIgnoreCase(keyword));
        }

        if (StringUtil.hasText(provinceCity)) {
            predicate.and(location.provinceCity.containsIgnoreCase(provinceCity));
        }

        if (organizationType != null) {
            predicate.and(company.organizationType.eq(organizationType));
        }

        if (industryType != null) {
            predicate.and(company.industryType.eq(industryType));
        }

        if (teamSize != null) {
            predicate.and(company.teamSize.eq(teamSize));
        }

        if (foundedIn != null) {
            predicate.and(company.foundedIn.eq(foundedIn));
        }

        predicate.and(company.user.deleteAt.isNull());

        // subquery đếm số lượng job active
        JPQLQuery<Long> jobCountSubQuery = jpaQueryFactory
                .select(job.company.id.count())
                .from(job)
                .where(job.company.id.eq(company.id).and(job.status.eq(JobStatus.ACTIVE)));
        
        var projection = Projections.constructor(CompanyPageResponse.class,
                        company.id,
                        company.name,
                        company.logoUrl,
                        company.industryType,
                        Projections.constructor(LocationDTO.class, 
                                                location.country, 
                                                location.provinceCity, 
                                                location.commune, 
                                                location.latitude, 
                                                location.longitude),
                        jobCountSubQuery);

        JPQLQuery<CompanyPageResponse> query = jpaQueryFactory
                .select(projection)
                .from(company)
                .leftJoin(company.location, location)
                .where(predicate);
        
        if (pageable.isPaged()) {
            query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        }

        List<CompanyPageResponse> results = query.fetch();

        long total = jpaQueryFactory
                .select(company.id.count())
                .from(company)
                .leftJoin(company.location, location)
                .where(predicate)
                .fetchOne();
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<AdminCompanyResponse> getCompaniesForAdmin(String email, Pageable pageable) {
        QCompany company = QCompany.company;
        QUser user = QUser.user;

        BooleanExpression isActive = JPAExpressions
                .selectOne()
                .from(user)
                .where(user.deleteAt.isNull())
                .where(user.id.eq(company.user.id))
                .exists();

        BooleanBuilder predicate = new BooleanBuilder();
        if (StringUtil.hasText(email)) {
            predicate.and(user.email.containsIgnoreCase(email));
        }

        var query = jpaQueryFactory
                .select(Projections.constructor(AdminCompanyResponse.class,
                        company.id,
                        isActive,
                        company.name,
                        user.email,
                        company.logoUrl,
                        user.createdAt))
                .from(company)
                .join(company.user, user)
                .where(predicate)
                .orderBy(company.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<AdminCompanyResponse> results = query.fetch();
        long total = jpaQueryFactory
                .select(company.id.count())
                .from(company)
                .join(company.user, user)
                .where(predicate)
                .fetchOne();
        return new PageImpl<>(results, pageable, total);
    }
    
}

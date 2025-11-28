package com.ptit.thesis.smartrecruit.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.dto.response.ConversationResponse;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.QCandidateProfile;
import com.ptit.thesis.smartrecruit.entity.QChatMessage;
import com.ptit.thesis.smartrecruit.entity.QCompany;
import com.ptit.thesis.smartrecruit.entity.QConversation;
import com.ptit.thesis.smartrecruit.enums.MessageDirection;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ConversationRepositoryCustomImpl implements ConversationRepositoryCustom {

    JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<ConversationResponse> findCandidateConversations(CandidateProfile candidate, Pageable pageable) {
        QConversation conversation = QConversation.conversation;
        QCandidateProfile qcandidate = QCandidateProfile.candidateProfile;
        QCompany company = QCompany.company;
        QChatMessage chatMessage = QChatMessage.chatMessage;

        Expression<Long> unreadCount = JPAExpressions
                        .select(chatMessage.count())
                        .from(chatMessage)
                        .where(
                                chatMessage.conversation.eq(conversation)
                                .and(chatMessage.isRead.eq(false))
                                .and(chatMessage.direction.eq(MessageDirection.FROM_EMPLOYER))
                        );

        BooleanBuilder predicate = new BooleanBuilder();
        
        predicate.and(qcandidate.id.eq(candidate.getId()));

        var query = jpaQueryFactory
                .select(Projections.constructor(ConversationResponse.class,
                    conversation.id,
                    company.id,
                    company.name,
                    company.logoUrl,
                    conversation.lastMessageContent,
                    conversation.updatedAt,
                    unreadCount
                ))
                .from(conversation)
                .join(conversation.company, company)
                .join(conversation.candidate, qcandidate)
                .where(predicate)
                .orderBy(conversation.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        List<ConversationResponse> results = query.fetch();
        
        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results = results.subList(0, pageable.getPageSize());
        }
        
        return new SliceImpl<>(results, pageable, hasNext);
    }

    @Override
    public Slice<ConversationResponse> findCompanyConversations(Company company, Pageable pageable) {
        QConversation conversation = QConversation.conversation;
        QCandidateProfile candidate = QCandidateProfile.candidateProfile;
        QCompany qcompany = QCompany.company;
        QChatMessage chatMessage = QChatMessage.chatMessage;

        Expression<Long> unreadCount = JPAExpressions
                        .select(chatMessage.count())
                        .from(chatMessage)
                        .where(
                                chatMessage.conversation.eq(conversation)
                                .and(chatMessage.isRead.eq(false))
                                .and(chatMessage.direction.eq(MessageDirection.FROM_CANDIDATE))
                        );

        BooleanBuilder predicate = new BooleanBuilder();
        
        predicate.and(qcompany.id.eq(company.getId()));

         // Long conservationId;
    // Long partnerId;
    // String partnerName;
    // String partnerAvatarUrl;
    // String lastMessage;
    // LocalDateTime lastMessageAt;
    // long unreadCount;
        var query = jpaQueryFactory
                .select(Projections.constructor(ConversationResponse.class, 
                    conversation.id,
                    candidate.id,
                    candidate.fullName,
                    candidate.avatarUrl,
                    conversation.lastMessageContent,
                    conversation.updatedAt,
                    unreadCount
                ))
                .from(conversation)
                .join(conversation.candidate, candidate)
                .join(conversation.company, qcompany)
                .where(predicate)
                .orderBy(conversation.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        List<ConversationResponse> results = query.fetch();
        
        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results = results.subList(0, pageable.getPageSize());
        }
        
        return new SliceImpl<>(results, pageable, hasNext);
    }
    
}

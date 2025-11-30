package com.ptit.thesis.smartrecruit.service.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.dto.request.ChatMessageRequest;
import com.ptit.thesis.smartrecruit.dto.response.ChatMessageResponse;
import com.ptit.thesis.smartrecruit.dto.response.ChatMessagesInitialResponse;
import com.ptit.thesis.smartrecruit.dto.response.ConversationResponse;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.ChatMessage;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.Conversation;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.MessageDirection;
import com.ptit.thesis.smartrecruit.enums.NotificationType;
import com.ptit.thesis.smartrecruit.exception.ResourceNotFoundException;
import com.ptit.thesis.smartrecruit.mapper.ChatMapper;
import com.ptit.thesis.smartrecruit.repository.CandidateProfileRepository;
import com.ptit.thesis.smartrecruit.repository.ChatMessageRepository;
import com.ptit.thesis.smartrecruit.repository.CompanyRepository;
import com.ptit.thesis.smartrecruit.repository.ConversationRepository;
import com.ptit.thesis.smartrecruit.service.ChatService;
import com.ptit.thesis.smartrecruit.service.NotificationService;
import com.ptit.thesis.smartrecruit.service.S3Service;
import com.ptit.thesis.smartrecruit.utils.Constant;
import com.ptit.thesis.smartrecruit.utils.StringUtil;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    NotificationService notificationService;

    CandidateProfileRepository candidateProfileRepository;
    CompanyRepository companyRepository;
    ConversationRepository conversationRepository;
    ChatMessageRepository chatMessageRepository;

    SimpMessagingTemplate messagingTemplate;
    S3Service s3Service;

    ChatMapper chatMapper;

    @Override
    @Transactional
    public ChatMessagesInitialResponse sendMessage(ChatMessageRequest request, User sender, boolean isInitialized) {
        boolean isCandidateSender = sender.getRole().getName().equals(Constant.CANDIDATE_ROLE);

        CandidateProfile candidate;
        Company company;
        User recipientUser;

        if (isCandidateSender) {
            candidate = candidateProfileRepository.findByUser(sender)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Candidate not found for user: " + sender.getId()));
            company = companyRepository.findById(request.getRecipientId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Company not found for id: " + request.getRecipientId()));
            recipientUser = (User) Hibernate.unproxy(company.getUser());
        } else {
            company = companyRepository.findByUser(sender)
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found for user: " + sender.getId()));
            candidate = candidateProfileRepository.findById(request.getRecipientId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Candidate not found for id: " + request.getRecipientId()));
            recipientUser = (User) Hibernate.unproxy(candidate.getUser());
        } 

        // tim hoac tao conversation
        Conversation conversation = conversationRepository.findByCandidateAndCompany(candidate, company)
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation();
                    newConversation.setCandidate(candidate);
                    newConversation.setCompany(company);
                    return conversationRepository.save(newConversation);
                });

        ChatMessage message = new ChatMessage();
        message.setConversation(conversation);
        message.setContent(request.getContent());
        message.setDirection(isCandidateSender ? MessageDirection.FROM_CANDIDATE : MessageDirection.FROM_EMPLOYER);
        message.setIsRead(false);
        ChatMessage savedChatMessage = chatMessageRepository.save(message);

        conversation.setLastMessageContent(request.getContent());
        conversationRepository.save(conversation);

        ChatMessageResponse response = chatMapper.toChatMessageResponse(savedChatMessage);

        messagingTemplate.convertAndSendToUser(recipientUser.getUsername(), "/queue/messages", response);

        notificationService.pushNotification(
                sender,
                recipientUser,
                "Bạn có một tin nhắn mới từ " + (isCandidateSender ? candidate.getFullName() : company.getName()),
                NotificationType.NEW_MESSAGE,
                conversation.getId());
        
        if (isInitialized) {
            Pageable pageable = PageRequest.of(0, 10);
            Slice<ChatMessageResponse> conversations = getConversationMessages(conversation.getId(), sender, pageable);
            ChatMessagesInitialResponse initlizatedresponse = new ChatMessagesInitialResponse();
            initlizatedresponse.setConversationId(conversation.getId());
            initlizatedresponse.setMessages(conversations.getContent());
            initlizatedresponse.setHasNext(conversations.hasNext());
            return initlizatedresponse;
        }
        
        return null;
    }

    @Override
    @Transactional
    public Slice<ConversationResponse> getUserConversations(User user, Pageable pageable, Boolean isRead, String keyword) {
        boolean isCandidateSender = user.getRole().getName().equals(Constant.CANDIDATE_ROLE);

        if (isCandidateSender) {
            CandidateProfile candidate = candidateProfileRepository.findByUser(user)
                    .orElseThrow(() -> new ResourceNotFoundException("Candidate not found for user: " + user.getId()));
            return conversationRepository.findCandidateConversations(candidate, pageable, isRead, keyword).map(
                    conversation -> {
                        conversation.setPartnerAvatarUrl(
                                s3Service.generatePresignedUrl(conversation.getPartnerAvatarUrl()));
                        String lastMessage = conversation.getLastMessage();
                        if (StringUtil.hasText(lastMessage)) {
                            if (lastMessage.length() > 30) {
                                conversation.setLastMessage(lastMessage.substring(0, 30) + " ...");
                            }
                        }
                        return conversation;
                    });
        } else {
            Company company = companyRepository.findByUser(user)
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found for user: " + user.getId()));
            return conversationRepository.findCompanyConversations(company, pageable, isRead, keyword).map(
                    conversation -> {
                        conversation.setPartnerAvatarUrl(
                                s3Service.generatePresignedUrl(conversation.getPartnerAvatarUrl()));
                        String lastMessage = conversation.getLastMessage();
                        if (StringUtil.hasText(lastMessage)) {
                            if (lastMessage.length() > 30) {
                                conversation.setLastMessage(lastMessage.substring(0, 30) + " ...");
                            }
                        }
                        return conversation;
                    });
        }
    }

    @Override
    @Transactional
    public Slice<ChatMessageResponse> getConversationMessages(Long conversationId, User user, Pageable pageable) {
        log.info("Getting chat messages from conversation with id " + conversationId);

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found for id: " + conversationId));

        boolean isCandidate = user.getRole().getName().equals(Constant.CANDIDATE_ROLE);
        validateUserInconversation(user, conversation, isCandidate);

        log.info("Getting message entity from database");
        Slice<ChatMessage> messages = chatMessageRepository.findByConversationIdOrderByCreatedAtDesc(conversationId, pageable);
        log.info("Getting message entity from database successfully.");

        log.info("Converting messages entity to response dto.");
        Slice<ChatMessageResponse> convertedMessages = messages.map(message -> chatMapper.toChatMessageResponse(message));
        log.info("Converting messages entity to response dto successfully.");

        log.info("Geting chat messages form conversation with id " + conversationId + " successfully");
        return convertedMessages;
    }

    @Override
    @Transactional
    public void markAsRead(Long conversationId, User user) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found for id: " + conversationId));

        boolean isCandidateSender = user.getRole().getName().equals(Constant.CANDIDATE_ROLE);

        validateUserInconversation(user, conversation, isCandidateSender);

        if (isCandidateSender) {
            setCandidateRead(conversation);
        } else {
            setCompanyRead(conversation);
        }
    }

    private void validateUserInconversation(User user, Conversation conversation, boolean isCandidate) {
        log.info("Starting to check if user with id " + user.getId() + " are in conversation with id " + conversation.getId());

        if (isCandidate) {
            if (!conversation.getCandidate().getUser().getId().equals(user.getId())) {
                log.warn("This user are not in the conversation.");
                throw new AccessDeniedException("You do not have permission to access this conversation");
            }
        } else {
            if (!conversation.getCompany().getUser().getId().equals(user.getId())) {
                log.warn("This user are not in the conversation.");
                throw new AccessDeniedException("You do not have permission to access this conversation");
            }
        }

        log.info("This user are already in this conversation.");
    }

    private void setCandidateRead(Conversation conversation) {
        List<ChatMessage> unreadMessages = chatMessageRepository.findUnreadMessages(conversation,
                MessageDirection.FROM_EMPLOYER);
        for (ChatMessage message : unreadMessages) {
            message.setIsRead(true);
        }
        chatMessageRepository.saveAll(unreadMessages);
    }

    private void setCompanyRead(Conversation conversation) {
        List<ChatMessage> unreadMessages = chatMessageRepository.findUnreadMessages(conversation,
                MessageDirection.FROM_CANDIDATE);
        for (ChatMessage message : unreadMessages) {
            message.setIsRead(true);
        }
        chatMessageRepository.saveAll(unreadMessages);
    }
}

package com.ptit.thesis.smartrecruit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ptit.thesis.smartrecruit.dto.response.ChatMessageResponse;
import com.ptit.thesis.smartrecruit.entity.ChatMessage;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "timestampt", source = "createdAt")
    ChatMessageResponse toChatMessageResponse(ChatMessage message);
}

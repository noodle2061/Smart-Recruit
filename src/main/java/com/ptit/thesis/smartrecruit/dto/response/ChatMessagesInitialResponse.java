package com.ptit.thesis.smartrecruit.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO chat messages initial response
 * Dùng trả về khi người dùng bắt đầu cuộc trò chuyện với người khác trong trang cá nhân của họ
 * mà không biết liệu đã tồn tại cuộc trò chuyện của hai người trước đó hay chưa
 * return: trả về slice tối đa 10 cuộc trò chuyện cuối cùng của cuộc hội thoại, bao gồm tin nhắn của người dùng vừa gửi
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessagesInitialResponse {
    Long conversationId;
    List<ChatMessageResponse> messages;
    boolean hasNext;
}

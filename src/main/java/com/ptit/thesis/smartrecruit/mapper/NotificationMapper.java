package com.ptit.thesis.smartrecruit.mapper;

import org.mapstruct.Mapper;

import com.ptit.thesis.smartrecruit.dto.response.NotificationResponse;
import com.ptit.thesis.smartrecruit.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    /**
     * Chuyen doi tu Notification -> NotificationResponse
     * <p>
     * các trường liên quan đến người gửi (sender) phải tự xử lý sau khi gọi hàm
     * @param notification
     * @return notification response dto
     */
    NotificationResponse toNotificationResponse(Notification notification);
}

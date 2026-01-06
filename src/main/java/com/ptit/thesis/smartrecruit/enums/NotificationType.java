package com.ptit.thesis.smartrecruit.enums;

public enum NotificationType {
    NEW_MESSAGE, // tin nhắn mới cho cả candidate và company
    JOB_APPLICATION, // thông báo cho trạng thái đơn ứng tuyển, cho cả candidate và company
    APPLICATION_STATUS_CHANGE, // trạng thái duyệt hay không duyệt, cho candidate
    SYSTEM_ALERT, // thông báo hệ thống, sự kiện,...
    NEW_COMMENT
}

package com.booking.BookingApp.models.dtos.users;

import com.booking.BookingApp.models.enums.NotificationTypeEnum;

import java.time.LocalDateTime;

public class NotificationPostDTO { //Notification model without id and date time
    public Long userId;
    public NotificationTypeEnum type;
    public String content;


    public NotificationPostDTO(Long userId, NotificationTypeEnum type, String content) {
        this.userId = userId;
        this.type = type;
        this.content = content;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public NotificationTypeEnum getType() {
        return type;
    }

    public void setType(NotificationTypeEnum type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

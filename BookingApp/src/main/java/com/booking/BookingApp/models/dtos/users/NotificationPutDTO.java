package com.booking.BookingApp.models.dtos.users;

import com.booking.BookingApp.models.enums.NotificationTypeEnum;

import java.time.LocalDateTime;

public class NotificationPutDTO { //Notification model without id
    public String userId;
    public String type;
    public String content;
    public LocalDateTime dateTime;
    public boolean read;

    public NotificationPutDTO(String userId, String type, String content, LocalDateTime dateTime, boolean read) {
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.dateTime = dateTime;
        this.read=read;
    }

    public NotificationPutDTO() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}

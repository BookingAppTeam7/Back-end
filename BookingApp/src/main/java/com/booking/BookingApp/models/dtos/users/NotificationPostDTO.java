package com.booking.BookingApp.models.dtos.users;

import com.booking.BookingApp.models.enums.NotificationTypeEnum;

import java.time.LocalDateTime;

public class NotificationPostDTO { //Notification model without id and date time
    public String userId;
    public String type;
    public String content;

    public LocalDateTime time;


    public NotificationPostDTO(String userId, String type, String content, LocalDateTime time) {
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.time = time;
    }

    public NotificationPostDTO() {
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}

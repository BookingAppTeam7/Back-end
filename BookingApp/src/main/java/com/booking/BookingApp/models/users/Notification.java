package com.booking.BookingApp.models.users;

import com.booking.BookingApp.models.enums.NotificationTypeEnum;

import java.time.LocalDateTime;

public class Notification {

    public Long id;
    public String userId;
    public String type;
    public String content;
    public LocalDateTime dateTime;

    public Notification(Long id, String userId, String type, String content, LocalDateTime dateTime) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.dateTime = dateTime;
    }

    public Notification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

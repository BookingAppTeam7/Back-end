package com.booking.BookingApp.models.users;

import com.booking.BookingApp.models.enums.NotificationTypeEnum;

import java.time.LocalDateTime;

public class Notification {

    public Long id;
    public Long userId;
    public NotificationTypeEnum type;
    public String content;
    public LocalDateTime dateTime;

    public Notification(Long id, Long userId, NotificationTypeEnum type, String content, LocalDateTime dateTime) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}

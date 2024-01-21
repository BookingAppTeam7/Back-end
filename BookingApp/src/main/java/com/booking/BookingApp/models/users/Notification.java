package com.booking.BookingApp.models.users;

import com.booking.BookingApp.models.enums.NotificationTypeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String userId;
    public String type;
    public String content;
    public LocalDateTime dateTime;
    public Boolean read;

    public Notification(Long id, String userId, String type, String content, LocalDateTime dateTime, Boolean read) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.dateTime = dateTime;
        this.read=read;
    }

    public Notification(String userId, String type, String content, LocalDateTime dateTime, boolean read) {
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.dateTime = dateTime;
        this.read=read;
    }

    public Notification() {
    }

    public Boolean isRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
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

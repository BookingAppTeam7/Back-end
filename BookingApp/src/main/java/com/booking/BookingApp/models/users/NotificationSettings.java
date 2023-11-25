package com.booking.BookingApp.models.users;

import com.booking.BookingApp.models.enums.NotificationTypeEnum;

import java.util.Map;

public class NotificationSettings {
    public Long id;
    public Long userId;
    public Map<NotificationTypeEnum,String> settings;

    public NotificationSettings(Long id, Long userId, Map<NotificationTypeEnum, String> settings) {
        this.id = id;
        this.userId = userId;
        this.settings = settings;
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

    public Map<NotificationTypeEnum, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<NotificationTypeEnum, String> settings) {
        this.settings = settings;
    }
}

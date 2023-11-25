package com.booking.BookingApp.models.users;

import com.booking.BookingApp.models.enums.NotificationTypeEnum;

import java.time.LocalDateTime;

public class Notification {

    public Long id;
    public Long userId;
    public NotificationTypeEnum type;
    public String content;
    public LocalDateTime dateTime;


}

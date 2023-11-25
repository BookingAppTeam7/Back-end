package com.booking.BookingApp.models.reservations;

import com.booking.BookingApp.models.enums.ReviewEnum;

import java.time.LocalDateTime;
import java.util.Date;

public class Review {
    public Long id;
    public Long userId;
    public ReviewEnum type;
    public String content;
    public LocalDateTime dateTime;

}

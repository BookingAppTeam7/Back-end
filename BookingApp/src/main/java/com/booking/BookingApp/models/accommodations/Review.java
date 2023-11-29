package com.booking.BookingApp.models.accommodations;

import com.booking.BookingApp.models.enums.ReviewEnum;

import java.time.LocalDateTime;
import java.util.Date;

public class Review {
    public Long id;
    public Long userId;
    public ReviewEnum type;
    public String comment;
    public int grade;
    public LocalDateTime dateTime;

    public Review(Long id, Long userId, ReviewEnum type, String comment,int grade, LocalDateTime dateTime) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.comment = comment;
        this.grade=grade;
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

    public ReviewEnum getType() {
        return type;
    }

    public void setType(ReviewEnum type) {
        this.type = type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}

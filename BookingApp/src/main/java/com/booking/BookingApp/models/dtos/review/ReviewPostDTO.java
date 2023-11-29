package com.booking.BookingApp.models.dtos.review;

import com.booking.BookingApp.models.enums.ReviewEnum;

import java.time.LocalDateTime;

public class ReviewPostDTO {//Review model without id, and dateTime

    public Long userId;
    public ReviewEnum type;
    public String comment;
    public int grade;

    public ReviewPostDTO(Long userId, ReviewEnum type, String comment, int grade) {
        this.userId = userId;
        this.type = type;
        this.comment = comment;
        this.grade = grade;
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

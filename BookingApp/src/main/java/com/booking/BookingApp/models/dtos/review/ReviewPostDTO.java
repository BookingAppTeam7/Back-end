package com.booking.BookingApp.models.dtos.review;

import com.booking.BookingApp.models.enums.ReviewEnum;
import com.booking.BookingApp.models.enums.ReviewStatusEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class ReviewPostDTO {//Review model without id, and dateTime

    public String userId;
    public ReviewEnum type;
    public String comment;
    public int grade;

    public LocalDateTime dateTime;
    public Boolean deleted;
    public Boolean reported;

    public Long accommodationId;
    public String ownerId;
    public ReviewStatusEnum status;

    public ReviewPostDTO(String userId, ReviewEnum type, String comment, int grade, LocalDateTime dateTime, Boolean deleted, Boolean reported, Long accommodationId, String ownerId,
                         ReviewStatusEnum status) {
        this.userId = userId;
        this.type = type;
        this.comment = comment;
        this.grade = grade;
        this.dateTime = dateTime;
        this.deleted = deleted;
        this.reported = reported;
        this.accommodationId = accommodationId;
        this.ownerId = ownerId;
        this.status=status;
    }

    public ReviewStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ReviewStatusEnum status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getReported() {
        return reported;
    }

    public void setReported(Boolean reported) {
        this.reported = reported;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}

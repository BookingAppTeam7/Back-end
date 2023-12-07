package com.booking.BookingApp.models.accommodations;

import com.booking.BookingApp.models.enums.ReviewEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String userId;
//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "accommodation_id")
//    private Accommodation accommodation;

    @Enumerated(EnumType.STRING)
    public ReviewEnum type;
    public String comment;
    public int grade;
    public LocalDateTime dateTime;

    public Review(Long id, String userId, ReviewEnum type, String comment,int grade, LocalDateTime dateTime) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.comment = comment;
        this.grade=grade;
        this.dateTime = dateTime;
    }

    public Review() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public ReviewEnum getType() {
        return type;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }
}

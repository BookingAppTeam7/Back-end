package com.booking.BookingApp.models.dtos.review;

public class ReviewPutDTO {//
    public String comment;
    public int grade;

    public ReviewPutDTO(String comment, int grade) {
        this.comment = comment;
        this.grade = grade;
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

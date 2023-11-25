package com.booking.BookingApp.models.reservations;

import java.util.List;

public class Feedback {
    public Long Id;
    public Long accommodationId;
    List<Review> reviews;

    public Feedback(Long id, Long accommodationId, List<Review> reviews) {
        Id = id;
        this.accommodationId = accommodationId;
        this.reviews = reviews;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}

package com.booking.BookingApp.models.reservations;

import java.util.List;

public class Feedback {
    public Long id;
    public Long accommodationId;
    List<Review> reviews;

    public Feedback(Long id, Long accommodationId, List<Review> reviews) {
        this.id = id;
        this.accommodationId = accommodationId;
        this.reviews = reviews;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
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

package com.booking.BookingApp.models.dtos.accommodations;

import com.booking.BookingApp.models.reservations.Review;

import java.util.List;

public class FeedbackPostDTO { //Feedback model without id
    public Long accommodationId;
    List<Review> reviews;

    public FeedbackPostDTO(Long id, Long accommodationId, List<Review> reviews) {

        this.accommodationId = accommodationId;
        this.reviews = reviews;
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

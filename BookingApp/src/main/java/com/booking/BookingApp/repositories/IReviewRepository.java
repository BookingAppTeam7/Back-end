package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.Review;

import java.util.List;
import java.util.Optional;

public interface IReviewRepository {
    List<Review> findAll();
    Optional<Review> findById(Long id);

    Optional<Review> save(Review createdReview);

    Review saveAndFlush(Review result);

    void deleteById(Long id);
}

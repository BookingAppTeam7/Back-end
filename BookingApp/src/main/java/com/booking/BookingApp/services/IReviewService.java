package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.dtos.review.ReviewPostDTO;
import com.booking.BookingApp.models.dtos.review.ReviewPutDTO;
import com.booking.BookingApp.models.enums.ReviewEnum;

import java.util.List;
import java.util.Optional;

public interface IReviewService {
    List<Review> findAll();
    Optional<Review> findById(Long id);
    Optional<Review> create(ReviewPostDTO newReview) throws Exception;
    Review update(ReviewPutDTO updatedReview, Long id, String userId, ReviewEnum type) throws Exception;
    void delete(Long id);
}

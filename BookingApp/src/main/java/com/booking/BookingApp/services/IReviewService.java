package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.AccommodationRequest;
import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.dtos.review.ReviewPostDTO;
import com.booking.BookingApp.models.dtos.review.ReviewPutDTO;
import com.booking.BookingApp.models.enums.ReviewStatusEnum;

import java.util.List;
import java.util.Optional;

public interface IReviewService {
    List<Review> findAll();
    Optional<Review> findById(Long id);
    Optional<Review> create(ReviewPostDTO newReview) throws Exception;
    Review update(ReviewPutDTO review, Long id);
    void delete(Long id);

    List<Review> findByOwnerId(String ownerId);
    List<Review> findByAccommodationId(Long accommodationId);
    List<Review> findByOwnerIdAndStatus(String ownerId, ReviewStatusEnum status);
    List<Review> findByAccommodationIdAndStatus(Long accommodationId, ReviewStatusEnum status);
    int updateStatus(Long reviewId, ReviewStatusEnum status);
}

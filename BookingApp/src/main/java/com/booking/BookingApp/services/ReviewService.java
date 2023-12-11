package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.dtos.review.ReviewPostDTO;
import com.booking.BookingApp.models.dtos.review.ReviewPutDTO;
import com.booking.BookingApp.models.enums.ReviewEnum;
import com.booking.BookingApp.repositories.IReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReviewService implements IReviewService{
    @Autowired
    public IReviewRepository reviewRepository;
    private static AtomicLong counter=new AtomicLong();
    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Optional<Review> create(ReviewPostDTO newReview) throws Exception {
        return Optional.empty();
    }

//    @Override
//    public Optional<Review> create(ReviewPostDTO newReview) throws Exception {
//        Long newId=(Long) counter.incrementAndGet();
//        Review createdReview=new Review(newId,newReview.userId,newReview.type,newReview.comment, newReview.grade, LocalDateTime.now());
//        return reviewRepository.save(createdReview);
//    }

    @Override
    public Review update(ReviewPutDTO updatedReview, Long id, String userId, ReviewEnum type) throws Exception {
        Review result=new Review(id,userId,type, updatedReview.comment, updatedReview.grade, LocalDateTime.now(),false);
        return reviewRepository.saveAndFlush(result);
    }

    @Override
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}

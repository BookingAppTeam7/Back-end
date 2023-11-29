package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.Review;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReviewRepository implements IReviewRepository{
    private List<Review> reviews=new ArrayList<>();
    private static AtomicLong counter=new AtomicLong();
    @Override
    public List<Review> findAll() {
        return this.reviews;
    }

    @Override
    public Optional<Review> findById(Long id) {
        Optional<Review> res=reviews.stream().filter(review -> review.getId()==id).findFirst();
        if(res.isPresent()){
            return res;
        }
        return null;
    }
    public Optional<Review> getById(Long id){
        return reviews.stream().filter(review -> review.getId() == id).findFirst();
    }
    @Override
    public Optional<Review> save(Review createdReview) {
        this.reviews.add(createdReview);
        return getById(createdReview.getId());
    }

    @Override
    public Review saveAndFlush(Review updatedReview) {
        Optional<Review> optionalReview=getById(updatedReview.id);
        if(optionalReview.isPresent()){
            Review review=optionalReview.get();
            review.setComment(updatedReview.comment);
            review.setType(updatedReview.type);
            review.setGrade(updatedReview.grade);
            review.setDateTime(updatedReview.dateTime);
            review.setUserId(updatedReview.userId);

            return review;
        }else{
            throw new RuntimeException("Review with ID "+updatedReview.id+" not found!");
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<Review> review=getById(id);
        if(review.isPresent()){
            Review reviewToDelete = review.get();
            reviews.remove(reviewToDelete);
        }
    }
}

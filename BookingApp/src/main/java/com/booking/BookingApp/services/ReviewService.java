package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.dtos.review.ReviewPostDTO;
import com.booking.BookingApp.models.dtos.review.ReviewPutDTO;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.models.enums.ReviewEnum;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IAccommodationRepository;
import com.booking.BookingApp.repositories.IReviewRepository;
import com.booking.BookingApp.repositories.IUserRepository;
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
    @Autowired
    public IUserRepository userRepository;
    @Autowired
    public IAccommodationRepository accommodationRepository;
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
        Review review=new Review(newReview.userId,newReview.type,newReview.comment,newReview.grade,newReview.dateTime,
                false,newReview.accommodationId,newReview.ownerId,false,newReview.status);
        reviewRepository.save(review);
        return  Optional.of(review);
    }

    @Override
    public Review update(ReviewPutDTO updatedReview, Long id)  {


        Review result=new Review(updatedReview.userId,updatedReview.type,updatedReview.comment,updatedReview.grade,
                updatedReview.dateTime
        ,updatedReview.deleted,updatedReview.accommodationId, updatedReview.ownerId,updatedReview.reported,updatedReview.status);

        return reviewRepository.saveAndFlush(result);
    }




    @Override
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public List<Review> findByOwnerId(String ownerId) {
        Optional<User> user=userRepository.findById(ownerId);
        if(!user.isPresent()){
            throw new IllegalArgumentException("User not found!");
        }
        return reviewRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Review> findByAccommodationId(Long accommodationId) {
        Optional<Accommodation> accommodation=accommodationRepository.findById(accommodationId);
        if(!accommodation.isPresent()){
            throw new IllegalArgumentException("Accommodation not found!");
        }
        return reviewRepository.findByAccommodationId(accommodationId);
    }
}

package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.dtos.review.ReviewPostDTO;
import com.booking.BookingApp.models.dtos.review.ReviewPutDTO;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.models.enums.ReviewEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IAccommodationRepository;
import com.booking.BookingApp.repositories.IReviewRepository;
import com.booking.BookingApp.repositories.IReservationRepository;
import com.booking.BookingApp.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    
    //public ReservationService reservationService;
  @Autowired
  public IReservationRepository reservationRepository;
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
        if(newReview.type.equals(ReviewEnum.ACCOMMODATION)){
            Optional<Reservation> res=reservationRepository.findById(newReview.reservationId);
            Reservation reservation=res.get();
            Date endDate = reservation.getTimeSlot().endDate;
            Date sevenDaysAgo = new Date();
            sevenDaysAgo.setDate(sevenDaysAgo.getDate() - 7);

            if (endDate.before(new Date()) && endDate.after(sevenDaysAgo) && newReview.type.equals(ReviewEnum.ACCOMMODATION)) {
                // Trenutni datum je unutar 7 dana od završetka rezervacije
                Review review=new Review(newReview.userId,newReview.type,newReview.comment,newReview.grade,newReview.dateTime,
                        false,newReview.accommodationId,newReview.ownerId,false,newReview.status);
                reviewRepository.save(review);
                return  Optional.of(review);

            } else {
                // Rok za postavljanje komentara i ocene je istekao
                return null;

            }
        }else{
            Review review=new Review(newReview.userId,newReview.type,newReview.comment,newReview.grade,newReview.dateTime,
                    false,newReview.accommodationId,newReview.ownerId,false,newReview.status);
            reviewRepository.save(review);
            return  Optional.of(review);
        }

    }

    @Override
    public Review update(ReviewPutDTO updatedReview, Long id)  {
        Review result=new Review(id,updatedReview.userId,updatedReview.type,updatedReview.comment,updatedReview.grade,
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
       List<Review> reviews=reviewRepository.findByOwnerId(ownerId);
        List<Review> res=new ArrayList<>();
        for(Review review: reviews){
            if(review.type.equals(ReviewEnum.OWNER)){
                res.add(review);
            }
        }
        return  res;

    }

    @Override
    public List<Review> findByAccommodationId(Long accommodationId) {
        Optional<Accommodation> accommodation=accommodationRepository.findById(accommodationId);
        if(!accommodation.isPresent()){
            throw new IllegalArgumentException("Accommodation not found!");
        }
        List<Review> reviews= reviewRepository.findByAccommodationId(accommodationId);
        List<Review> res=new ArrayList<>();
        for(Review review: reviews){
            if(review.type.equals(ReviewEnum.ACCOMMODATION)){
                res.add(review);
            }
        }
        return  res;
    }
}

package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.accommodations.AccommodationRequest;
import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.dtos.review.*;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.models.enums.AccommodationRequestStatus;
import com.booking.BookingApp.models.enums.ReviewStatusEnum;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.services.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private IReviewService reviewService;

    @GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Review>> findAll(){
        List<Review> reviews=reviewService.findAll();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Review> findById(@PathVariable Long id){
        Optional<Review> result=reviewService.findById(id);
        if(result==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.get(),HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Review> create(@RequestBody ReviewPostDTO newReview) throws Exception{
        Optional<Review> result=reviewService.create(newReview);
        if(result==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);
    }
    @PutMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Review> update(@RequestBody ReviewPutDTO review, @PathVariable Long id) throws Exception {

        Review result=reviewService.update(review,id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result,HttpStatus.OK);

    }

    @DeleteMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Review> delete(@PathVariable Long id){
        reviewService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value="owner/{ownerId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Review>> findByOwnerId(@PathVariable String ownerId){
        List<Review> result=reviewService.findByOwnerId(ownerId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping(value="accommodation/{accommodationId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Review>> findByAccommodationId(@PathVariable Long accommodationId){
        List<Review> result=reviewService.findByAccommodationId(accommodationId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping(value="owner/pending/{ownerId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Review>> findPendingByOwnerId(@PathVariable String ownerId){
        List<Review> result=reviewService.findByOwnerIdAndStatus(ownerId,ReviewStatusEnum.PENDING);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping(value="accommodation/pending/{accommodationId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Review>> findPendingByAccommodationId(@PathVariable Long accommodationId){
        List<Review> result=reviewService.findByAccommodationIdAndStatus(accommodationId,ReviewStatusEnum.PENDING);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/update-status/approve")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> approve(@PathVariable("id") Long reviewId){
        try {
            int result = reviewService.updateStatus(reviewId, ReviewStatusEnum.APPROVED);

            if (result!=0) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}/update-status/reject")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> reject(@PathVariable("id") Long reviewId){
        try {
            int result = reviewService.updateStatus(reviewId, ReviewStatusEnum.REJECTED);

            if (result!=0) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="mobile/owner/{ownerId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<ReviewGetDTO>> getByOwnerId(@PathVariable String ownerId){
        List<Review> result=reviewService.findByOwnerId(ownerId);
        List<ReviewGetDTO>reviews=new ArrayList<ReviewGetDTO>();

        for(Review r:result){
            ReviewGetDTO newReview=new ReviewGetDTO();
            newReview.setId(r.getId());
            newReview.setDateTime(r.getDateTime().toString());
            newReview.setComment(r.getComment());
            newReview.setGrade(r.getGrade());
            newReview.setUserId(r.getUserId());
            newReview.setType(r.getType());
            newReview.setOwnerId(r.getOwnerId());
            newReview.setReported(r.getReported());
            newReview.setAccommodationId(r.getAccommodationId());
            newReview.setStatus(r.getStatus());

            reviews.add(newReview);

        }

        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }

    @GetMapping(value="mobile/accommodation/{accommodationId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<ReviewGetDTO>> getByAccommodationId(@PathVariable Long accommodationId){
        List<Review> result=reviewService.findByAccommodationId(accommodationId);
        List<ReviewGetDTO>reviews=new ArrayList<ReviewGetDTO>();

        for(Review r:result){
            ReviewGetDTO newReview=new ReviewGetDTO();
            newReview.setId(r.getId());
            newReview.setDateTime(r.getDateTime().toString());
            newReview.setComment(r.getComment());
            newReview.setGrade(r.getGrade());
            newReview.setUserId(r.getUserId());
            newReview.setType(r.getType());
            newReview.setOwnerId(r.getOwnerId());
            newReview.setReported(r.getReported());
            newReview.setAccommodationId(r.getAccommodationId());
            newReview.setStatus(r.getStatus());

            reviews.add(newReview);

        }

        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }

    @GetMapping(value="averageGradesAccommodations/{accommodationId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Double> findAverageGradeByAccommodationId(@PathVariable Long accommodationId){
        double result=reviewService.findAverageGradeByAccommodationId(accommodationId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }


    @GetMapping(value="averageGradesOwners/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Double> findAverageGradeByOwnerId(@PathVariable String username){
        double result=reviewService.findAverageGradeByOnwerId(username);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }



    @PostMapping(value="reviewsMobileApp",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Review> createMobileAppsOwner(@RequestBody ReviewPostMobileDTO newReviewMobile) throws Exception{
        ReviewPostDTO newReview=new ReviewPostDTO(newReviewMobile.userId,newReviewMobile.type, newReviewMobile.comment,
                newReviewMobile.grade, LocalDateTime.now(),false,false, newReviewMobile.accommodationId, newReviewMobile.ownerId,
                newReviewMobile.status,0L);
        Optional<Review> result=reviewService.create(newReview);
        if(result==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);
    }

    @PostMapping(value="accommodations/reviewsMobileApp",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Review> createMobileAppsAccommodation(@RequestBody ReviewPostMobileAccommodationDTO newReviewMobile) throws Exception{
        ReviewPostDTO newReview=new ReviewPostDTO(newReviewMobile.userId,newReviewMobile.type, newReviewMobile.comment,
                newReviewMobile.grade, LocalDateTime.now(),false,false, newReviewMobile.accommodationId, newReviewMobile.ownerId,
                newReviewMobile.status,newReviewMobile.reservationId);
        Optional<Review> result=reviewService.create(newReview);
        if(result==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);
    }

    @PutMapping(value="mobileApps/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Review> update(@RequestBody ReviewPutMobileAppsDTO reviewMobile, @PathVariable Long id) throws Exception {
        ReviewPutDTO review=new ReviewPutDTO(reviewMobile.userId,reviewMobile.type, reviewMobile.comment, reviewMobile.grade,
                LocalDateTime.now(),reviewMobile.deleted,reviewMobile.reported, reviewMobile.accommodationId, reviewMobile.ownerId,reviewMobile.status);
        Review result=reviewService.update(review,id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result,HttpStatus.OK);

    }

}

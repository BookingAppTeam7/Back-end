package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.dtos.review.ReviewPostDTO;
import com.booking.BookingApp.models.dtos.review.ReviewPutDTO;
import com.booking.BookingApp.services.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private IReviewService reviewService;

    @GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Review>> findAll(){
        List<Review> reviews=reviewService.findAll();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Review> findById(@PathVariable Long id){
        Optional<Review> result=reviewService.findById(id);
        if(result==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.get(),HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Review> create(@RequestBody ReviewPostDTO newReview) throws Exception{
        Optional<Review> result=reviewService.create(newReview);
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);
    }
    @PutMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Review> update(@RequestBody ReviewPutDTO review, @PathVariable Long id) throws Exception{
        Optional<Review> previous=reviewService.findById(id);
        if(previous.isPresent()){
            Review result=reviewService.update(review,id,previous.get().userId, previous.get().type);
            if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
            return new ResponseEntity<>(result,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Review> delete(@PathVariable Long id){
        reviewService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

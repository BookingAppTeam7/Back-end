package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.dtos.accommodations.FeedbackPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.FeedbackPutDTO;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.models.reservations.Feedback;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.services.IFeedbackService;
import com.booking.BookingApp.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    @Autowired
    private IFeedbackService feedbackService;
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Feedback>> findAll(){
        List<Feedback> result=feedbackService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Feedback> findById(@PathVariable Long id){
        Optional<Feedback> result=feedbackService.findById(id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result.get(),HttpStatus.OK );
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Feedback> create(@RequestBody FeedbackPostDTO newFeedback) throws Exception {
        Optional<Feedback> result=feedbackService.create(newFeedback);
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);

    }

    @PutMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Feedback> update(@RequestBody FeedbackPutDTO feedback, @PathVariable Long id) throws Exception {
        Feedback result=feedbackService.update(feedback,id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result,HttpStatus.OK);

    }
    @DeleteMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Feedback> delete(@PathVariable Long id){
        feedbackService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

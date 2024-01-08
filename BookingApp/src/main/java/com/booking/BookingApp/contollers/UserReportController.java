package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.accommodations.AccommodationRequest;
import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.dtos.reports.UserReportPostDTO;
import com.booking.BookingApp.models.dtos.review.ReviewPostDTO;
import com.booking.BookingApp.models.enums.AccommodationRequestStatus;
import com.booking.BookingApp.models.reports.UserReport;
import com.booking.BookingApp.services.IUserReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/userReports")
public class UserReportController {
    @Autowired
    public IUserReportService userReportService;
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<UserReport> create(@RequestBody UserReportPostDTO newUserReport) throws Exception{
        Optional<UserReport> result=userReportService.create(newUserReport);
        if(result==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
   @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserReport>> findAll(){
        List<UserReport> requests=userReportService.findAll();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }


    @PutMapping(value = "/report/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
   // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserReport> deactivation(
            @PathVariable("id") Long requestId) {
        try {
            Optional<UserReport> result = Optional.ofNullable(userReportService.report(requestId));

            if (result.isPresent()) {
                return new ResponseEntity<>(result.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/ignore/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserReport> ignoreReport(
            @PathVariable("id") Long requestId) {
        try {
            Optional<UserReport> result = Optional.ofNullable(userReportService.ignore(requestId));

            if (result.isPresent()) {
                return new ResponseEntity<>(result.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="user/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserReport>> findByUser(@PathVariable ("id") String userId){
        List<UserReport> requests=userReportService.findByUser(userId);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }



}

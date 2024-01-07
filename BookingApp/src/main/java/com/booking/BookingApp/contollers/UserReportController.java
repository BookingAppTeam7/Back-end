package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.dtos.reports.UserReportPostDTO;
import com.booking.BookingApp.models.dtos.review.ReviewPostDTO;
import com.booking.BookingApp.models.reports.UserReport;
import com.booking.BookingApp.services.IUserReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}

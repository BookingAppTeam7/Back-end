package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.dtos.accommodations.TimeSlotPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.TimeSlotPutDTO;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.services.ITimeSlotService;
import com.booking.BookingApp.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/timeSlots")
public class TimeSlotController {

    @Autowired
    private ITimeSlotService timeSlotService;
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TimeSlot>> findAll(){
        List<TimeSlot> timeSlots=timeSlotService.findAll();
        return new ResponseEntity<>(timeSlots, HttpStatus.OK);
    }

    @GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeSlot> findById(@PathVariable Long id){
        Optional<TimeSlot> result=timeSlotService.findById(id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result.get(),HttpStatus.OK );
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeSlot> create(@RequestBody TimeSlotPostDTO newTimeSlot) throws Exception {
        Optional<TimeSlot> result=timeSlotService.create(newTimeSlot);
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);

    }

    @PutMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeSlot> update(@RequestBody TimeSlotPutDTO timeSlot, @PathVariable Long id) throws Exception {
        TimeSlot result=timeSlotService.update(timeSlot,id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result,HttpStatus.OK);

    }
    @DeleteMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeSlot> delete(@PathVariable Long id){
        timeSlotService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

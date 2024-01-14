package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.dtos.users.NotificationPostDTO;
import com.booking.BookingApp.models.dtos.users.NotificationPutDTO;
import com.booking.BookingApp.models.users.Notification;
import com.booking.BookingApp.services.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")

public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins="http://localhost:4200")
    public ResponseEntity<List<Notification>> findAll(){
        List<Notification> result=notificationService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins="http://localhost:4200")
    public ResponseEntity<Notification> findById(@PathVariable Long id){
        Optional<Notification> result=notificationService.findById(id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result.get(),HttpStatus.OK );
    }


    @GetMapping(value="/user/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins="http://localhost:4200")
    public ResponseEntity<List<Notification>> findByUserId(@PathVariable String id){
        List<Notification> result=notificationService.findByUserId(id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result,HttpStatus.OK );
    }

    @CrossOrigin(origins="http://localhost:4200")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Notification> create(@RequestBody NotificationPostDTO newNotification) throws Exception {
        Optional<Notification> result=notificationService.create(newNotification);
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);

    }

    @CrossOrigin(origins="http://localhost:4200")
    @PutMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Notification> update(@RequestBody NotificationPutDTO notification, @PathVariable Long id) throws Exception {
        Notification result=notificationService.update(notification,id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result,HttpStatus.OK);

    }
    @CrossOrigin(origins="http://localhost:4200")
    @PutMapping(value="read/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Notification> readNotification(@PathVariable Long id) {
        try {
            notificationService.read(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @CrossOrigin(origins="http://localhost:4200")
    @DeleteMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Notification> delete(@PathVariable Long id){
        notificationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

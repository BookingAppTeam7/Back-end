package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.dtos.reservations.ReservationGetDTO;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.dtos.reservations.ReservationPostDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationPutDTO;
import com.booking.BookingApp.models.users.Notification;
import com.booking.BookingApp.services.IAccommodationService;
import com.booking.BookingApp.services.IReservationService;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private IReservationService reservationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_OWNER')")
    public ResponseEntity<List<Reservation>> findAll(){
        List<Reservation> reservations=reservationService.findAll();
        return new ResponseEntity<List<Reservation>>(reservations, HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_GUEST')")
    public Optional<Reservation> findById(@PathVariable Long id){
        return reservationService.findById(id);
    }

    @PutMapping(value="/confirm/{id}")
    @CrossOrigin(origins="http://localhost:4200")
    public ResponseEntity<?> confirmReservation(@PathVariable Long id) {
        try{
            System.out.println("PRE CONFIRM RESERVATION");
            reservationService.confirmReservation(id);
            return ResponseEntity.ok("Reservation updated! Check database of accommodation and reservation");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",e.getMessage()));
        }

    }
    @PostMapping
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_GUEST')")
    public ResponseEntity<?> create(@RequestBody ReservationPostDTO newReservation) throws Exception {
        try {
            Optional<Reservation> createdReservation = reservationService.create(newReservation);
            return ResponseEntity.ok(createdReservation.orElse(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }

    }

    @PutMapping(value="/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_GUEST')")
    public Reservation update(@RequestBody ReservationPutDTO reservation, @PathVariable Long id) throws Exception {
        return reservationService.update(reservation,id);

    }
    @DeleteMapping(value="/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_GUEST')")
    public ResponseEntity<Reservation> delete(@PathVariable Long id){
        reservationService.delete(id);
        return new ResponseEntity<Reservation>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value="/accommodation/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_GUEST')")
    public ResponseEntity<List<Reservation>> findByAccommodationId(@PathVariable Long id){
        List<Reservation> result=reservationService.findByAccommodationId(id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        System.out.println(result);
        return new ResponseEntity<>(result,HttpStatus.OK );
    }

    @GetMapping(value="/user/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_GUEST')")
    public ResponseEntity<List<Reservation>> findByGuestId(@PathVariable String username){
        List<Reservation> result=reservationService.findByGuestId(username);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result,HttpStatus.OK );
    }

    @PutMapping(value="reject/{id}")
    @CrossOrigin(origins="http://localhost:4200")
    public ResponseEntity<?> rejectReservation(@PathVariable Long id) {
        try{
            reservationService.rejectReservation(id);
            return ResponseEntity.ok("Reservation updated! Check database of accommodation and reservation");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",e.getMessage()));
        }

    }

    @PutMapping(value="cancel/{id}")
    @CrossOrigin(origins="http://localhost:4200")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        try{
            reservationService.cancelReservation(id);
            return ResponseEntity.ok("Reservation updated! Check database of accommodation and reservation");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",e.getMessage()));
        }

    }
    @GetMapping(value="/searchFilter",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins="http://localhost:4200")
    public ResponseEntity<List<Reservation>> searchFilter(
            @RequestParam(required = false) String accName,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) ReservationStatusEnum status
            ){
        System.out.println(accName);
        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println(status);

        try{
            Date arrival = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate);
            Date checkout = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate);

            if (checkout.before(arrival)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            List<Reservation> reservations=reservationService.searchFilter(accName,arrival,checkout,status);
            return new ResponseEntity<>(reservations,HttpStatus.OK);
        }catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

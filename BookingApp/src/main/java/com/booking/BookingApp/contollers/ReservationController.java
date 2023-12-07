package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.dtos.reservations.ReservationPostDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationPutDTO;
import com.booking.BookingApp.services.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private IReservationService reservationService;
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Reservation>> findAll(){
        List<Reservation> reservations=reservationService.findAll();
        return new ResponseEntity<List<Reservation>>(reservations, HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public Optional<Reservation> findById(@PathVariable Long id){
        return reservationService.findById(id);
    }


    @PostMapping
    @CrossOrigin(origins = "http://localhost:4200")
    public Optional<Reservation> create(@RequestBody ReservationPostDTO newReservation) throws Exception {
        return reservationService.create(newReservation);

    }

    @PutMapping(value="/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public Reservation update(@RequestBody ReservationPutDTO reservation, @PathVariable Long id) throws Exception {
        return reservationService.update(reservation,id);

    }
    @DeleteMapping(value="/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Reservation> delete(@PathVariable Long id){
        reservationService.delete(id);
        return new ResponseEntity<Reservation>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value="accommodation/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Reservation>> findByAccommodationId(@PathVariable Long id){
        List<Reservation> result=reservationService.findByAccommodationId(id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result,HttpStatus.OK );
    }
}

package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.Accommodation;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.services.IAccommodationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accommodations")
public class AccommodationController {
    @Autowired
    private IAccommodationService accommodationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Accommodation>> findAll(){
        List<Accommodation> accommodations=accommodationService.findAll();
        return new ResponseEntity<List<Accommodation>>(accommodations, HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    public Optional<Accommodation> findById(@PathVariable Long id){return accommodationService.findById(id);}

    @PostMapping
    public Optional<Accommodation> create(@RequestBody AccommodationPostDTO newAccommodation) throws Exception{
        return accommodationService.create(newAccommodation);
    }
    @PutMapping(value="/{id}")
    public Accommodation update(@RequestBody AccommodationPutDTO accommodation, @PathVariable Long id) throws Exception{
        return accommodationService.update(accommodation,id);
    }
    @DeleteMapping(value="/{id}")
    public ResponseEntity<Accommodation> delete(@PathVariable Long id){
        accommodationService.delete(id);
        return new ResponseEntity<Accommodation>(HttpStatus.NO_CONTENT);
    }

}

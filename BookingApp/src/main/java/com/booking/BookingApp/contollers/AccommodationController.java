package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.PriceCard;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.services.IAccommodationService;
import com.booking.BookingApp.services.IPriceCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accommodations")
public class AccommodationController {
    @Autowired
    private IAccommodationService accommodationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Accommodation>> findAll(){  //treba u get zahtevu za availability podesiti samo one TIMESLOTOVE gde je tip AVAILABILITY
        List<Accommodation> accommodations=accommodationService.findAll();
        return new ResponseEntity<List<Accommodation>>(accommodations, HttpStatus.OK);
    }

    @GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public Optional<Accommodation> findById(@PathVariable Long id){return accommodationService.findById(id);}

    @PostMapping
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public Optional<Accommodation> create(@RequestBody AccommodationPostDTO newAccommodation) throws Exception{
        return accommodationService.create(newAccommodation);
    }
    @PutMapping(value="/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public Accommodation update(@RequestBody AccommodationPutDTO accommodation, @PathVariable Long id) throws Exception{
        return accommodationService.update(accommodation,id);
    }
    @DeleteMapping(value="/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<Accommodation> delete(@PathVariable Long id){
        accommodationService.delete(id);
        return new ResponseEntity<Accommodation>(HttpStatus.NO_CONTENT);
    }

}

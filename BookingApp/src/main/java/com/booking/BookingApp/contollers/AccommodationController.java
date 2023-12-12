package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.PriceCard;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.services.IAccommodationService;
import com.booking.BookingApp.services.IPriceCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
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

    @GetMapping(value="status/{status}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Accommodation>> findByStatus(@PathVariable AccommodationStatusEnum status){
        List<Accommodation> accommodations=accommodationService.findByStatus(status);
        return new ResponseEntity<List<Accommodation>>(accommodations, HttpStatus.OK);
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Accommodation>  create(@RequestBody AccommodationPostDTO newAccommodation) throws Exception{
        Optional<Accommodation> result = accommodationService.create(newAccommodation);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping(value="/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Accommodation> update(@RequestBody AccommodationPutDTO accommodation, @PathVariable Long id) throws Exception{
        Optional<Accommodation> result = accommodationService.update(accommodation, id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/{id}/update-status")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Accommodation> updateStatus(
            @PathVariable("id") Long accommodationId,
            @RequestParam("status") AccommodationStatusEnum status) {
        try {
            Optional<Accommodation> result = accommodationService.updateStatus(accommodationId, status);

            if (result.isPresent()) {
                return new ResponseEntity<>(result.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value="/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Accommodation> delete(@PathVariable Long id){
        accommodationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

package com.booking.BookingApp.contollers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.AccommodationDetails;
import com.booking.BookingApp.models.accommodations.PriceCard;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.enums.TypeEnum;
import com.booking.BookingApp.services.IAccommodationService;
import com.booking.BookingApp.services.IPriceCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Date;
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
    @GetMapping(value="/search",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<AccommodationDetails>> search(@RequestParam(required = false) String city,
                                                             @RequestParam int guests,
                                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date arrival,
                                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkout){  //treba u get zahtevu za availability podesiti samo one TIMESLOTOVE gde je tip AVAILABILITY
        System.out.println(city);
        System.out.println(guests);
        System.out.println(arrival);
        System.out.println(checkout);
        if (checkout.before(arrival) || arrival.before(new Date()) || checkout.before(new Date()) || guests<1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<AccommodationDetails> accommodations=accommodationService.search(city,guests,arrival,checkout);
        return new ResponseEntity<>(accommodations, HttpStatus.OK);
    }
    @GetMapping(value="/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<AccommodationDetails>> filter(
            @RequestParam(required = false) String searched,
            @RequestParam(required = false) String assets,
            @RequestParam(required = false) TypeEnum type,
            @RequestParam(required = false, defaultValue = "-1.0") String minTotalPrice,
            @RequestParam(required = false, defaultValue = "-1.0") String maxTotalPrice) {

        List<String> assetSplit = Arrays.asList(assets != null ? assets.split(",") : new String[]{});
        List<AccommodationDetails> searchedList;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            searchedList = objectMapper.readValue(searched, new TypeReference<List<AccommodationDetails>>() {
            });
            double minPrice = Double.parseDouble(minTotalPrice);
            double maxPrice = Double.parseDouble(maxTotalPrice);

            List<AccommodationDetails> accommodations = accommodationService.filter(searchedList, assetSplit, type, minPrice, maxPrice);
            return new ResponseEntity<>(accommodations, HttpStatus.OK);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
    @DeleteMapping(value="/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Accommodation> delete(@PathVariable Long id){
        accommodationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

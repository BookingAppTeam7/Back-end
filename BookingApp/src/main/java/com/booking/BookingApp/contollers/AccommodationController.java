package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.accommodations.Review;
import com.fasterxml.jackson.core.type.TypeReference;
import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.AccommodationDetails;
import com.booking.BookingApp.models.accommodations.PriceCard;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.enums.TypeEnum;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.services.IAccommodationService;
import com.booking.BookingApp.services.IPriceCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/accommodations")
public class AccommodationController {

    @Autowired
    private IAccommodationService accommodationService;

    private List<AccommodationDetails> searchedAccommodations=new ArrayList<>();
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Accommodation>> findAll(){
        List<Accommodation> accommodations=accommodationService.findAll();
        return new ResponseEntity<>(accommodations, HttpStatus.OK);
    }
    @GetMapping(value="/approved",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Accommodation>> findAllApproved(){
        List<Accommodation> accommodations=accommodationService.findAllApproved();
        return new ResponseEntity<>(accommodations, HttpStatus.OK);
    }
    
    @GetMapping(value="/search",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<AccommodationDetails>> search(
            @RequestParam(required = false) String city,
            @RequestParam int guests,
            @RequestParam  String arrivalString,
            @RequestParam  String checkoutString) {
        System.out.println(city);
        System.out.println(guests);
        System.out.println(arrivalString);
        System.out.println(checkoutString);

        try {
            Date arrival = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(arrivalString);
            Date checkout = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(checkoutString);

            if (checkout.before(arrival) || arrival.before(new Date()) || checkout.before(new Date()) || guests < 1) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            List<AccommodationDetails> accommodations = accommodationService.search(city, guests, arrival, checkout);
            searchedAccommodations=accommodationService.search(city, guests, arrival, checkout);
            return new ResponseEntity<>(accommodations, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value="/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<AccommodationDetails>> filter(
            //@RequestParam(required = false) String searched,
            @RequestParam(required = false) String assets,
            @RequestParam(required = false) TypeEnum type,
            @RequestParam(required = false, defaultValue = "-1.0") String minTotalPrice,
            @RequestParam(required = false, defaultValue = "-1.0") String maxTotalPrice) {

        List<String> assetSplit = Arrays.asList(assets != null ? assets.split(",") : new String[]{});
        //List<AccommodationDetails> searchedList;
        try {
         //   ObjectMapper objectMapper = new ObjectMapper();
         //   objectMapper.registerModule(new JavaTimeModule());
         //   searchedList = objectMapper.readValue(searched, new TypeReference<List<AccommodationDetails>>() {
         //   });
            double minPrice = Double.parseDouble(minTotalPrice);
            double maxPrice = Double.parseDouble(maxTotalPrice);
            for(AccommodationDetails ad:searchedAccommodations)
                System.out.println(ad);
            List<AccommodationDetails> accommodations = accommodationService.filter(searchedAccommodations, assetSplit, type, minPrice, maxPrice);
            return new ResponseEntity<>(accommodations, HttpStatus.OK);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Accommodation> findById(@PathVariable Long id){
        Optional<Accommodation>result=accommodationService.findById(id);
        if(result!=null){return new ResponseEntity<>(result.get(),HttpStatus.OK);}
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="status/{status}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Accommodation>> findByStatus(@PathVariable AccommodationStatusEnum status){
        List<Accommodation> accommodations=accommodationService.findByStatus(status);
        return new ResponseEntity<>(accommodations, HttpStatus.OK);
    }

    @GetMapping(value="owner/{ownerId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<List<Accommodation>> findByOwnerId(@PathVariable String ownerId){
        List<Accommodation> accommodations=accommodationService.findByOwnerId(ownerId);
        if (accommodations!=null) {
            return new ResponseEntity<>(accommodations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    
    @PostMapping
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
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
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<Accommodation> update(@RequestBody AccommodationPutDTO accommodation, @PathVariable Long id) throws Exception{
        Optional<Accommodation> result = accommodationService.update(accommodation, id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/{id}/update-status")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    @PutMapping(value="/{id}/add-image")
    @CrossOrigin(origins="http://localhost:4200")
    //@PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<Accommodation> insertImage(
            @PathVariable("id") Long accommodationId,
            @RequestParam("image") String images){
        try{
            List<String> newImages= List.of(images.split(","));
            Optional<Accommodation> result=accommodationService.updateImages(accommodationId,newImages);
            if(result.isPresent()){
                return new ResponseEntity<>(result.get(),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value="/{id}/add-review")
    @CrossOrigin(origins="http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_GUEST')")
    public ResponseEntity<Accommodation> insertReview(
            @PathVariable("id") Long accommodationId,
            @RequestBody Review review){
        try{
            Optional<Accommodation> result=accommodationService.addReview(accommodationId,review);
            if(result.isPresent()){
                return new ResponseEntity<>(result.get(),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @DeleteMapping(value="/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<Accommodation> delete(@PathVariable Long id){
        accommodationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

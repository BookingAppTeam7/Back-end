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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<List<Accommodation>> findAll(){
        List<Accommodation> accommodations=accommodationService.findAll();
        return new ResponseEntity<>(accommodations, HttpStatus.OK);
    }

    
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
            objectMapper.registerModule(new JavaTimeModule());
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

   // @PutMapping(value="/{id}/add-image")
   // @CrossOrigin(origins="http://localhost:4200")
   // public ResponseEntity<Accommodation> insertImage(
   //         @PathVariable("id") Long accommodationId,
   //         @RequestParam("image") String image){
   //     try{
   //         Optional<Accommodation> result=accommodationService.updateImages(accommodationId,image);
   //         if(result.isPresent()){
    //            return new ResponseEntity<>(result.get(),HttpStatus.OK);
    //        }else{
    //            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
   //         }
    //    }catch (Exception e){
    //        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //    }
   // }

    @PostMapping(value="/{id}/add-image")
    @CrossOrigin(origins="http://localhost:4200")
    public ResponseEntity<String> addNewImage(
            @PathVariable("id") Long accommodationId,
            @RequestParam("image") MultipartFile imageFile) throws IOException{

        accommodationService.addNewImage(accommodationId,imageFile);
        return new ResponseEntity<>("Image successfully added!",HttpStatus.OK);
    }
    @PutMapping(value="/{id}/add-review")
    @CrossOrigin(origins="http://localhost:4200")
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

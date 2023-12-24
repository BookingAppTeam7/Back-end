package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.PriceCard;
import com.booking.BookingApp.models.dtos.accommodations.*;
import com.booking.BookingApp.services.IPriceCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/priceCards")

public class PriceCardController {
    @Autowired
    private IPriceCardService priceCardService;
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PriceCard>> findAll(){
        List<PriceCard> result=priceCardService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_OWNER')")
    public ResponseEntity<PriceCard> findById(@PathVariable Long id){
        Optional<PriceCard> result=priceCardService.findById(id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result.get(),HttpStatus.OK );
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<PriceCard> create(@RequestBody PriceCardPostDTO newPriceCard) throws Exception {
        Optional<PriceCard> result=priceCardService.create(newPriceCard);
        if(result==null){return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value="/DatesString",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<PriceCard> add(@RequestBody PriceCardStringDTO newPriceCardString) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date startDate = sdf.parse(newPriceCardString.getTimeSlot().getStartDate());
        Date endDate = sdf.parse(newPriceCardString.getTimeSlot().getStartDate());

        TimeSlotPostDTO newTimeSlot=new TimeSlotPostDTO(startDate,endDate);

        PriceCardPostDTO newPriceCard=new PriceCardPostDTO(newTimeSlot, newPriceCardString.price, newPriceCardString.type, newPriceCardString.accommodationId);

        Optional<PriceCard> result=priceCardService.create(newPriceCard);
        if(result==null){return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);
    }

    @PutMapping(value="/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<PriceCard> update(@RequestBody PriceCardPutDTO accommodation, @PathVariable Long id) throws Exception{
        Optional<PriceCard> result = priceCardService.update(accommodation, id);
        if (result!=null) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
//    @CrossOrigin(origins = "http://localhost:4200")
//    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<PriceCard> update(@RequestBody PriceCardPutDTO priceCard) throws Exception {
//        PriceCard result=priceCardService.update(priceCard);
//        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
//        return new ResponseEntity<>(result,HttpStatus.OK);
//    }
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<PriceCard> delete(@PathVariable Long id){
        priceCardService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @GetMapping(value="accommodation/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
//    @CrossOrigin(origins = "http://localhost:4200")
//    public ResponseEntity<List<PriceCard>> findByAccommodationId(@PathVariable Long id){
//        List<PriceCard> result=priceCardService.findByAccommodationId(id);
//        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
//        return new ResponseEntity<>(result,HttpStatus.OK );
//    }




}

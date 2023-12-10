package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.accommodations.PriceCard;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPutDTO;
import com.booking.BookingApp.services.IPriceCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/priceCards")

public class PriceCardController {
    @Autowired
    private IPriceCardService priceCardService;
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<PriceCard>> findAll(){
        List<PriceCard> result=priceCardService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PriceCard> findById(@PathVariable Long id){
        Optional<PriceCard> result=priceCardService.findById(id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result.get(),HttpStatus.OK );
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PriceCard> create(@RequestBody PriceCardPostDTO newPriceCard) throws Exception {
        Optional<PriceCard> result=priceCardService.create(newPriceCard);
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);

    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PriceCard> update(@RequestBody PriceCard priceCard) throws Exception {
        PriceCard result=priceCardService.update(priceCard);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @DeleteMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
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

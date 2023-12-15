package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.AccommodationRequest;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationRequestPostDTO;
import com.booking.BookingApp.models.enums.AccommodationRequestStatus;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.services.IAccommodationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accommodationsRequests")
public class AccommodationRequestController {
    @Autowired
    private IAccommodationRequestService requestService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<AccommodationRequest>> findAll(){
        List<AccommodationRequest> requests=requestService.findAll();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<AccommodationRequest> findById(@PathVariable Long id){
        Optional<AccommodationRequest> result=requestService.findById(id);
        if(result!=null){return new ResponseEntity<>(result.get(),HttpStatus.OK);}
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="status/{status}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<AccommodationRequest>> findByStatus(@PathVariable AccommodationRequestStatus status){
        List<AccommodationRequest> accommodations=requestService.findByStatus(status);
        return new ResponseEntity<>(accommodations, HttpStatus.OK);
    }

    @GetMapping(value = "/status/{status1}/{status2}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<AccommodationRequest>> findByStatus(
            @PathVariable AccommodationRequestStatus status1,
            @PathVariable AccommodationRequestStatus status2) {
        List<AccommodationRequest> accommodations = requestService.findByRequestStatus(status1, status2);
        return new ResponseEntity<>(accommodations, HttpStatus.OK);
    }

//    @PostMapping
//    @CrossOrigin(origins = "http://localhost:4200")
//    public ResponseEntity<AccommodationRequest>  create(@RequestBody AccommodationRequestPostDTO newRequest) throws Exception{
//        Optional<AccommodationRequest> result = requestService.create(newRequest);
//        if (result.isPresent()) {
//            return new ResponseEntity<>(result.get(), HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    @PutMapping(value = "/{id}/update-status")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<AccommodationRequest> updateStatus(
            @PathVariable("id") Long requestId,
            @RequestParam("status") AccommodationRequestStatus status) {
        try {
            Optional<AccommodationRequest> result = requestService.updateStatus(requestId, status);

            if (result.isPresent()) {
                return new ResponseEntity<>(result.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

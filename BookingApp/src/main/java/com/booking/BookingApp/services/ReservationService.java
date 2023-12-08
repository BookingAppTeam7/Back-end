package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.dtos.reservations.ReservationPostDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationPutDTO;
import com.booking.BookingApp.repositories.IReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService implements IReservationService{

    @Autowired
    public IReservationRepository reservationRepository;

    private AccommodationService accommodationService=new AccommodationService();

    @Autowired
    public ReservationService(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    private static AtomicLong counter=new AtomicLong();
    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> findById(Long id) {

        Optional<Reservation> res= reservationRepository.findById(id);
        if(res.isPresent()){
            return res;
        }
        return null;
    }

    @Override
    public Optional<Reservation> create(ReservationPostDTO newReservation) throws Exception {
        Long newId= (Long) counter.incrementAndGet();
        //AccommodationService accommodationService = new AccommodationService();
        Accommodation accommodation = this.accommodationService.findById(newReservation.getAccommodationId())
                .orElseThrow(() -> new Exception("Accommodation not found with id: " + newReservation.getAccommodationId()));
        Reservation createdReservation=new Reservation(newId,newReservation.userId,newReservation.timeSlot, ReservationStatusEnum.PENDING, accommodation, newReservation.numberOfGuests);
        return Optional.of(reservationRepository.save(createdReservation));
    }

    @Override
    public Reservation update(ReservationPutDTO updatedReservation, Long id) throws Exception {
       // Reservation result=new Reservation(id,updatedReservation.userId,updatedReservation.timeSlot,updatedReservation.status);
       //return reservationRepository.saveAndFlush(result);
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new Exception("Reservation not found with id: " + id));
        existingReservation.setUserId(updatedReservation.getUserId());
        existingReservation.setTimeSlot(updatedReservation.getTimeSlot());
        existingReservation.setStatus(updatedReservation.getStatus());
        existingReservation.setNumberOfGuests(updatedReservation.getNumberOfGuests());
        // Save and flush the updated reservation
        return reservationRepository.saveAndFlush(existingReservation);
    }

    @Override
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public List<Reservation> findByAccommodationId(Long id){
        return reservationRepository.findByAccommodationId(id);
    }
}

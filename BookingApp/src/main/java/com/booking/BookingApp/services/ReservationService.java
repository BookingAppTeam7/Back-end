package com.booking.BookingApp.services;

import com.booking.BookingApp.exceptions.NotFoundException;
import com.booking.BookingApp.exceptions.ValidationException;
import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.dtos.reservations.ReservationPostDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationPutDTO;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService implements IReservationService{

    @Autowired
    public IReservationRepository reservationRepository;

    private AccommodationService accommodationService=new AccommodationService();
    private UserService userService=new UserService();
    @Autowired
    public ReservationService(AccommodationService accommodationService,UserService userService) {
        this.accommodationService = accommodationService;
        this.userService=userService;
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
    public Optional<Reservation> create(ReservationPostDTO newReservation) throws NotFoundException, ValidationException {
        Long newId= (Long) counter.incrementAndGet();
        Accommodation accommodation = this.accommodationService.findById(newReservation.getAccommodationId())
                .orElseThrow(() -> new NotFoundException("Accommodation not found with id: " + newReservation.getAccommodationId()));
        UserGetDTO user=this.userService.findById(newReservation.getUserId())
                .orElseThrow(()->new NotFoundException("User not found with id: "+newReservation.getUserId()));
        User foundUser=this.userService.findByToken(user.token).orElseThrow(()->new NotFoundException("User not found with token: "+user.token));
        if(newReservation.timeSlot.startDate.after(newReservation.timeSlot.endDate))
            throw new ValidationException("Time slot is incorrect!");
        if(!accommodationService.hasAvailableTimeSlot(accommodation,newReservation.timeSlot.getStartDate(),newReservation.timeSlot.getEndDate()))
            throw new ValidationException("Accommodation not available in selected time slot!");

        if(newReservation.getNumberOfGuests()>accommodation.maxGuests || newReservation.getNumberOfGuests()<accommodation.minGuests)
            throw new ValidationException("Accommodation not available for that many guests!");
        //ako postoji potvrdjena rezervacija na isti smestaj ciji se timeslot preklapa sa ovim novim, baci Exception
        List<Reservation> allReservations=reservationRepository.findAll();
        for(Reservation r:allReservations){
            if(r.status.equals(ReservationStatusEnum.APPROVED) && r.accommodation.id.equals(accommodation.id) && timeSlotsOverlap(r.timeSlot,newReservation.timeSlot))
                throw new ValidationException("There already exists confirmed reservation for that accommodation in selected time slot");
        }
        Reservation createdReservation=new Reservation(newId,newReservation.timeSlot, ReservationStatusEnum.PENDING, accommodation, newReservation.numberOfGuests,
                    foundUser);
        return Optional.of(reservationRepository.save(createdReservation));
    }
    public boolean timeSlotsOverlap(TimeSlot t1,TimeSlot t2){
        return !(t1.endDate.before(t2.startDate) || t1.startDate.after(t2.endDate));
        //if(t1.endDate.before(t2.startDate) || t1.startDate.after(t2.endDate))
        //    return false;
        //return true;
    }
    @Override
    public Reservation update(ReservationPutDTO updatedReservation, Long id) throws Exception {
       // Reservation result=new Reservation(id,updatedReservation.userId,updatedReservation.timeSlot,updatedReservation.status);
       //return reservationRepository.saveAndFlush(result);
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new Exception("Reservation not found with id: " + id));
        //existingReservation.setUserId(updatedReservation.getUserId());
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

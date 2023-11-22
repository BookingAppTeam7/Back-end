package com.booking.BookingApp.services;

import com.booking.BookingApp.models.Reservation;
import com.booking.BookingApp.models.User;
import com.booking.BookingApp.models.dtos.UserGetDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationPostDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationPutDTO;
import com.booking.BookingApp.models.enums.StatusEnum;
import com.booking.BookingApp.repositories.IReservationRepository;
import com.booking.BookingApp.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService implements IReservationService{

    @Autowired
    public IReservationRepository reservationRepository;
    private static AtomicLong counter=new AtomicLong();
    @Override
    public List<Reservation> findAll() {
         return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public Optional<Reservation> create(ReservationPostDTO newReservation) throws Exception {
        Long newId= (Long) counter.incrementAndGet();
        Reservation createdReservation=new Reservation(newId,newReservation.accommodationId,newReservation.userId,newReservation.startDate,newReservation.endDate);
        return reservationRepository.save(createdReservation);
    }

    @Override
    public Reservation update(ReservationPutDTO updatedReservation, Long id) throws Exception {
        Reservation result=new Reservation(id,updatedReservation.accommodationId,updatedReservation.userId,updatedReservation.startDate,updatedReservation.endDate);
        return reservationRepository.saveAndFlush(result);
    }

    @Override
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}

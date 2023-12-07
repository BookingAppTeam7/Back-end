package com.booking.BookingApp.services;

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
        Reservation createdReservation=new Reservation(newId,newReservation.userId,newReservation.timeSlot, ReservationStatusEnum.PENDING);
        return Optional.of(reservationRepository.save(createdReservation));
    }

    @Override
    public Reservation update(ReservationPutDTO updatedReservation, Long id) throws Exception {
        Reservation result=new Reservation(id,updatedReservation.userId,updatedReservation.timeSlot,updatedReservation.status);
        return reservationRepository.saveAndFlush(result);
    }

    @Override
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}

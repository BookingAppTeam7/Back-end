package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.reservations.Reservation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository implements IReservationRepository{

    private List<Reservation> reservations=new ArrayList<>();

    @Override
    public List<Reservation> findAll() {
        return this.reservations;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservations.stream().filter(reservation-> reservation.getId() == id).findFirst();
    }

    @Override
    public Optional<Reservation> save(Reservation createdReservation) {
        this.reservations.add(createdReservation);
        return findById(createdReservation.getId());
    }

    @Override
    public Reservation saveAndFlush(Reservation updatedReservation) {
        Optional<Reservation> optionalReservation=findById(updatedReservation.id);

        if (optionalReservation.isPresent()) {
            Reservation reservation= optionalReservation.get();
            reservation.setAccommodationId(updatedReservation.getAccommodationId());
            reservation.setStartDate(updatedReservation.getStartDate());
            reservation.setEndDate(updatedReservation.getEndDate());

            return reservation;
        }
        else{
            throw new RuntimeException("Korisnik s ID-om " + updatedReservation.id + " nije pronađen.");
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<Reservation> reservation=findById(id);
        if(reservation.isPresent()){
            Reservation reservationToDelete = reservation.get();
            reservations.remove(reservationToDelete);
        }
    }

    }


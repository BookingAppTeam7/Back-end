package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.reservations.Reservation;

import java.util.List;
import java.util.Optional;

public interface IReservationRepository {

    List<Reservation> findAll();

   Optional<Reservation> findById(Long id);

    Optional<Reservation> save(Reservation createdReservation);

    Reservation saveAndFlush(Reservation result);

    void deleteById(Long id);
}

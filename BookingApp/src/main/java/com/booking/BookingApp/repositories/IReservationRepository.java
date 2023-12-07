package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.reservations.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findByAccommodationId(Long id);
}

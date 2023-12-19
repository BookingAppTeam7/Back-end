package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface IReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findByAccommodationId(Long id);
    List<Reservation> findByUserUsername(String username);
    List<Reservation> findByStatus(ReservationStatusEnum status);
}

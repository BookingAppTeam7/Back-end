package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface IReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findByAccommodationId(Long id);
    List<Reservation> findByUserUsername(String username);
    List<Reservation> findByStatus(ReservationStatusEnum status);

    @Modifying
    @Transactional
    @Query("UPDATE Reservation r SET r.status = :newStatus WHERE r.id = :reservationId")
    int updateStatus(@Param("reservationId") Long reservationId, @Param("newStatus") ReservationStatusEnum newStatus);

}

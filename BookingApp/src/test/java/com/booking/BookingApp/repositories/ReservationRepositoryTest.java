package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.reservations.Reservation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class ReservationRepositoryTest {
    @Autowired
    private IReservationRepository reservationRepository;
    @Test
    @Sql("classpath:test-data-reservation.sql")
    public void updateStatusTest() {
        Long reservationId = 2L;
        ReservationStatusEnum newStatus = ReservationStatusEnum.APPROVED;

        int affectedRows = reservationRepository.updateStatus(reservationId, newStatus);

        assertEquals(1,affectedRows);

        Optional<Reservation> updatedReservation = reservationRepository.findById(reservationId);
        assertThat(updatedReservation).isPresent();
        assertThat(updatedReservation.get().getStatus()).isEqualTo(newStatus);
    }
}




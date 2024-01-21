package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.enums.*;
import com.booking.BookingApp.models.reservations.Reservation;

import com.booking.BookingApp.models.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class ReservationRepositoryTest {
    @Autowired
    private IReservationRepository reservationRepository;

    @Test
    public void updateStatusTest() {
        Long reservationId = 6L;
        ReservationStatusEnum newStatus = ReservationStatusEnum.APPROVED;

        int affectedRows = reservationRepository.updateStatus(reservationId, newStatus);

        assertEquals(1,affectedRows);

        Optional<Reservation> updatedReservation = reservationRepository.findById(reservationId);
        assertThat(updatedReservation).isPresent();
        assertThat(updatedReservation.get().getStatus()).isEqualTo(newStatus);
    }
    @Test
    public void shouldSaveReservation(){
        Timestamp startDate = Timestamp.from(Instant.parse("2023-02-10T10:57:00Z"));
        Timestamp endDate = Timestamp.from(Instant.parse("2023-02-15T10:57:00Z"));
        Boolean deleted = false;
        TimeSlot timeSlot = new TimeSlot(10L,startDate, endDate, deleted);
        Location location=new Location(5L,"adresa","grad","drzava",15.5,25.5,false);
        Accommodation accommodation=new Accommodation(1L,"ime1","opis1",null,2,5, TypeEnum.ROOM,new ArrayList<String>(),new ArrayList<PriceCard>(),"OWNER@gmail.com",5, ReservationConfirmationEnum.MANUAL,new ArrayList<Review>(),new ArrayList<String>(),false, AccommodationStatusEnum.APPROVED);
        User user=new User("TESTGOST1@gmail.com","guest", RoleEnum.GUEST);
        Reservation reservation=new Reservation(10L,accommodation,null,timeSlot,ReservationStatusEnum.PENDING,5L,5000,PriceTypeEnum.PERGUEST);

        Reservation savedReservation=reservationRepository.save(reservation);
        assertThat(savedReservation).usingRecursiveComparison().ignoringFields("id").isEqualTo(reservation);
    }
}




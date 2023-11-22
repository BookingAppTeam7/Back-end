package com.booking.BookingApp.services;

import com.booking.BookingApp.models.Reservation;
import com.booking.BookingApp.models.User;
import com.booking.BookingApp.models.dtos.UserGetDTO;
import com.booking.BookingApp.models.dtos.UserPostDTO;
import com.booking.BookingApp.models.dtos.UserPutDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationPostDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationPutDTO;

import java.util.List;
import java.util.Optional;

public interface IReservationService {
    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Optional<Reservation> create(ReservationPostDTO newReservation) throws Exception;
    Reservation update(ReservationPutDTO updatedReservation, Long  id) throws  Exception;
    void delete(Long id);
}

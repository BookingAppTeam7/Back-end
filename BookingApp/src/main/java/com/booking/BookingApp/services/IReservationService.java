package com.booking.BookingApp.services;

import com.booking.BookingApp.models.dtos.reservations.ReservationGetDTO;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.dtos.reservations.ReservationPostDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationPutDTO;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IReservationService {
    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Optional<Reservation> create(ReservationPostDTO newReservation) throws Exception;
    Reservation update(ReservationPutDTO updatedReservation, Long  id) throws  Exception;
    void delete(Long id);
    List<Reservation> findByAccommodationId(Long id);
    Reservation confirmReservation(Long reservationId) throws Exception;
    List<Reservation> findByGuestId(String username);

    void rejectReservation(Long reservationId) throws Exception;
    void cancelReservation(Long reservationId) throws Exception;
    List<Reservation> searchFilter(String accName, Date startDate, Date endDate, ReservationStatusEnum status);

    void editPriceCards(Long accommodationId, Date reservationStartDate, Date reservationEndDate);
}

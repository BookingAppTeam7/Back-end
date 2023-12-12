package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;

import java.util.List;
import java.util.Optional;

public interface IAccommodationService {
    List<Accommodation> findAll();

    Optional<Accommodation> findById(Long id);
    List<Accommodation> findByStatus(AccommodationStatusEnum status);

    Optional<Accommodation> create(AccommodationPostDTO newUser) throws Exception;
    Optional<Accommodation> update(AccommodationPutDTO updatedUser, Long  id) throws  Exception;
    void delete(Long id);
    Optional<Accommodation> updateStatus(Long accommodationId,AccommodationStatusEnum status);
}

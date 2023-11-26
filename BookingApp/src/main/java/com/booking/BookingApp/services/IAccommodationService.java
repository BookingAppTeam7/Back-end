package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;

import java.util.List;
import java.util.Optional;

public interface IAccommodationService {
    List<Accommodation> findAll();

    Optional<Accommodation> findById(Long id);

    Optional<Accommodation> create(AccommodationPostDTO newUser) throws Exception;
    Accommodation update(AccommodationPutDTO updatedUser, Long  id) throws  Exception;
    void delete(Long id);
}

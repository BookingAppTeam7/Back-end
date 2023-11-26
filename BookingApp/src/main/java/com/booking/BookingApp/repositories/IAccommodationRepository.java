package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.Accommodation;

import java.util.List;
import java.util.Optional;

public interface IAccommodationRepository {
    List<Accommodation> findAll();

    Optional<Accommodation> findById(Long id);

    Optional<Accommodation> save(Accommodation createdAccommodation);

    Accommodation saveAndFlush(Accommodation result);

    void deleteById(Long id);
}

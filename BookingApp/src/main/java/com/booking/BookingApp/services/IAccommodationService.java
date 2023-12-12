package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.AccommodationDetails;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.enums.TypeEnum;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IAccommodationService {
    List<Accommodation> findAll();

    Optional<Accommodation> findById(Long id);

    Optional<Accommodation> create(AccommodationPostDTO newUser) throws Exception;
    Optional<Accommodation> update(AccommodationPutDTO updatedUser, Long  id) throws  Exception;
    void delete(Long id);
    List<AccommodationDetails> search(String city, int guests, Date arrivalDate, Date checkoutDate);
    List<AccommodationDetails> filter(List<AccommodationDetails> searched, List<String> assets, TypeEnum type, double minTotalPrice,double maxTotalPrice);
}

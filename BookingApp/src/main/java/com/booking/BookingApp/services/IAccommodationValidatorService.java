package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPostDTO;

public interface IAccommodationValidatorService {
    void validate(AccommodationPostDTO accommodation);
    void validatePriceCard(PriceCardPostDTO newPriceCard);
}

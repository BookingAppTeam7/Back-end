package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPutDTO;

public interface IAccommodationValidatorService {
    void validatePost(AccommodationPostDTO accommodation);
    void validatePut(AccommodationPutDTO accommodation,Long id);
    void validatePriceCardPost(PriceCardPostDTO newPriceCard);
    void validatePriceCardPut(PriceCardPutDTO newPriceCard);
}
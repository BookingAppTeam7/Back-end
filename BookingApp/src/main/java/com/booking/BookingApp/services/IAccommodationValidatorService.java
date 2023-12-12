package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPutDTO;

public interface IAccommodationValidatorService {
    boolean validatePost(AccommodationPostDTO accommodation);
    boolean validatePut(AccommodationPutDTO accommodation,Long id);
    boolean validatePriceCardPost(PriceCardPostDTO newPriceCard);
    boolean validatePriceCardPut(PriceCardPutDTO newPriceCard,Long id);
}

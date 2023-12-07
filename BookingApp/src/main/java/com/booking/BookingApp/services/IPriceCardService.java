package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.PriceCard;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.PriceCardPutDTO;


import java.util.List;
import java.util.Optional;

public interface IPriceCardService {
    List<PriceCard> findAll();
    Optional<PriceCard> findById(Long id);
    Optional<PriceCard> create(PriceCardPostDTO newPriceCard) throws Exception;
    PriceCard update(PriceCardPutDTO updatedPriceCard, Long  id) throws  Exception;
    void delete(Long id);

    //List<PriceCard> findByAccommodationId(Long id);
}

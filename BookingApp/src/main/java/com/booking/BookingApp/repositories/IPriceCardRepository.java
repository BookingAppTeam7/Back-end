package com.booking.BookingApp.repositories;


import com.booking.BookingApp.models.accommodations.PriceCard;

import java.util.List;
import java.util.Optional;

public interface IPriceCardRepository {

    List<PriceCard> findAll();

    Optional<PriceCard> findById(Long id);

    Optional<PriceCard> save(PriceCard createdPriceCard);

    PriceCard saveAndFlush(PriceCard result);

    void deleteById(Long id);
}

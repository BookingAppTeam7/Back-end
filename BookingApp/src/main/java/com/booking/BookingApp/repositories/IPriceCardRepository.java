package com.booking.BookingApp.repositories;


import com.booking.BookingApp.models.accommodations.PriceCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IPriceCardRepository extends JpaRepository<PriceCard,Long> {

}

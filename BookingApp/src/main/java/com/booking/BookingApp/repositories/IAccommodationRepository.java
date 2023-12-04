package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAccommodationRepository extends JpaRepository<Accommodation,Long> {
    

}

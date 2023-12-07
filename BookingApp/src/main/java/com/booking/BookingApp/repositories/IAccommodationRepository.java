package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAccommodationRepository extends JpaRepository<Accommodation,Long> {
    

}

package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILocationRepository extends JpaRepository<Location, Long> {

}

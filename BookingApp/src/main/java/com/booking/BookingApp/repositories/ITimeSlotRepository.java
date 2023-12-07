package com.booking.BookingApp.repositories;


import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
@Repository
public interface ITimeSlotRepository extends JpaRepository<TimeSlot, Long> {

}

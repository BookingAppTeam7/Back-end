package com.booking.BookingApp.repositories;


import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.users.User;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

public interface ITimeSlotRepository {
    List<TimeSlot> findAll();

    Optional<TimeSlot> findById(Long id);

    Optional<TimeSlot> save(TimeSlot createdTimeSlot);

    TimeSlot saveAndFlush(TimeSlot result);

    void deleteById(Long id);
}

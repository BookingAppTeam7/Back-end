package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.dtos.accommodations.TimeSlotPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.TimeSlotPutDTO;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.models.users.User;

import java.util.List;
import java.util.Optional;

public interface ITimeSlotService {
    List<TimeSlot> findAll();

    Optional<TimeSlot> findById(Long id);

    Optional<TimeSlot> create(TimeSlotPostDTO newTimeSlot) throws Exception;
    TimeSlot update(TimeSlotPutDTO updatedTimeSlot, Long  id) throws  Exception;
    void delete(Long id);
}

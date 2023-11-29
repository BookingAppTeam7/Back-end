package com.booking.BookingApp.models.dtos.reservations;

import com.booking.BookingApp.models.accommodations.TimeSlot;

import java.sql.Time;
import java.util.Date;

public class ReservationPostDTO { //Reservation model without id and status (default --> PENDING)

    public  Long accommodationId;
    public Long userId;
    public TimeSlot timeSlot;


    public ReservationPostDTO(Long id, Long accommodationId, Long userId, TimeSlot timeSlot) {

        this.accommodationId = accommodationId;
        this.userId=userId;
        this.timeSlot=timeSlot;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getAccommodationId() {
        return accommodationId;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

}

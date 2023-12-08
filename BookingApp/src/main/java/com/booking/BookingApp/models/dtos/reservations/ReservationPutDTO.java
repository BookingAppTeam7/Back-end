package com.booking.BookingApp.models.dtos.reservations;

import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;

import java.util.Date;

public class ReservationPutDTO { //Reservation model without id

    public  Long accommodationId;
    public Long userId;
    public TimeSlot timeSlot;

    public ReservationStatusEnum status;
    public Long numberOfGuests;
    public ReservationPutDTO(Long id, Long accommodationId, Long userId,TimeSlot timeSlot,ReservationStatusEnum status, Long numberOfGuests) {

        this.accommodationId = accommodationId;
        this.userId=userId;
        this.timeSlot=timeSlot;
        this.status=status;
        this.numberOfGuests=numberOfGuests;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Long numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
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


    public ReservationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ReservationStatusEnum status) {
        this.status = status;
    }
}

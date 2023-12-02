package com.booking.BookingApp.models.dtos.reservations;

import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;

public class ReservationGetDTO {
    public Long id;
    public  Long accommodationId;

    public TimeSlot timeSlot;

    public ReservationStatusEnum status;

    public ReservationGetDTO(Long id, Long accommodationId,TimeSlot timeSlot,ReservationStatusEnum status) {
        this.id = id;
        this.accommodationId = accommodationId;
        this.timeSlot=timeSlot;
        this.status=status;
    }

    public Long getId() {
        return id;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public ReservationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ReservationStatusEnum status) {
        this.status = status;
    }
}

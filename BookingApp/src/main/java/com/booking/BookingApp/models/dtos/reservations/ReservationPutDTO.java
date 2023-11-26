package com.booking.BookingApp.models.dtos.reservations;

import com.booking.BookingApp.models.enums.ReservationStatusEnum;

import java.util.Date;

public class ReservationPutDTO { //Reservation model without id

    public  Long accommodationId;
    public Long userId;
    public Date startDate;

    public Date endDate;

    public ReservationStatusEnum status;

    public ReservationPutDTO(Long id, Long accommodationId, Long userId,Date startDate, Date endDate,ReservationStatusEnum status) {

        this.accommodationId = accommodationId;
        this.userId=userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status=status;
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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }



    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ReservationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ReservationStatusEnum status) {
        this.status = status;
    }
}

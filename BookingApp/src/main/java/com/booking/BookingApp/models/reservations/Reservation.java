package com.booking.BookingApp.models.reservations;

import java.util.Date;

public class Reservation {

    public Long id;
    public  Long accommodationId;
    public Long userId;
    public Date startDate;
    public Date endDate;

    public Reservation(Long id, Long accommodationId,Long userId, Date startDate, Date endDate) {
        this.id = id;
        this.accommodationId = accommodationId;
        this.userId=userId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setId(Long id) {
        this.id = id;
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
}

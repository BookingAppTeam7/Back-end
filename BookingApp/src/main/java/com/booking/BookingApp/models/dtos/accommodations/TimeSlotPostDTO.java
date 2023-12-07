package com.booking.BookingApp.models.dtos.accommodations;

import com.booking.BookingApp.models.enums.TimeSlotType;

import java.util.Date;

public class TimeSlotPostDTO {


    public Date startDate;
    public Date endDate;


    public TimeSlotPostDTO(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;

    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}

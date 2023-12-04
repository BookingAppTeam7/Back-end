package com.booking.BookingApp.models.dtos.accommodations;

import com.booking.BookingApp.models.enums.TimeSlotType;

import java.util.Date;

public class TimeSlotPutDTO {
    public Date startDate;
    public Date endDate;
    public TimeSlotType type;

    public TimeSlotPutDTO(Date startDate, Date endDate,TimeSlotType type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.type=type;
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

    public TimeSlotType getType() {
        return type;
    }

    public void setType(TimeSlotType type) {
        this.type = type;
    }
}

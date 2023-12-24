package com.booking.BookingApp.models.dtos.accommodations;

import com.booking.BookingApp.models.CustomDateDeserializer;
import com.booking.BookingApp.models.enums.TimeSlotType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class TimeSlotPostDTO {


    //@JsonDeserialize(using = CustomDateDeserializer.class)
    public Date startDate;

    //@JsonDeserialize(using = CustomDateDeserializer.class)
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

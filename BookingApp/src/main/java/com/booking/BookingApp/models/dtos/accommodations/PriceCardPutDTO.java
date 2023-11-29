package com.booking.BookingApp.models.dtos.accommodations;

import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.enums.PriceTypeEnum;

public class PriceCardPutDTO {
    public TimeSlot timeSlot;
    public double price;
    public PriceTypeEnum type;

    public PriceCardPutDTO(TimeSlot timeSlot, double price, PriceTypeEnum type) {
        this.timeSlot=timeSlot;
        this.price = price;
        this.type = type;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PriceTypeEnum getType() {
        return type;
    }

    public void setType(PriceTypeEnum type) {
        this.type = type;
    }
}

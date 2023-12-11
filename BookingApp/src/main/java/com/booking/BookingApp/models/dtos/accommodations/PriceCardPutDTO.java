package com.booking.BookingApp.models.dtos.accommodations;

import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.enums.PriceTypeEnum;

public class PriceCardPutDTO {
    public Long id;
    public TimeSlot timeSlot;
    public double price;
    public PriceTypeEnum type;

    public Long accommodationId;

    public PriceCardPutDTO(Long id,TimeSlot timeSlot, double price, PriceTypeEnum type,Long accommodationId) {
        this.id=id;
        this.timeSlot=timeSlot;
        this.price = price;
        this.type = type;
        this.accommodationId=accommodationId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }
}

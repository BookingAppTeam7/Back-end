package com.booking.BookingApp.models.dtos.accommodations;

import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.enums.PriceTypeEnum;

public class PriceCardPostDTO {
    public TimeSlotPostDTO timeSlot;
    public double price;
    public PriceTypeEnum type;

    public Long accommodationId;

    public PriceCardPostDTO(TimeSlotPostDTO timeSlot, double price, PriceTypeEnum type,Long accommodationId) {
        this.timeSlot=timeSlot;
        this.price = price;
        this.type = type;
        this.accommodationId=accommodationId;
    }

    public TimeSlotPostDTO getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlotPostDTO timeSlot) {
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

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }
}

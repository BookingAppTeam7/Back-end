package com.booking.BookingApp.models.dtos.accommodations;

import com.booking.BookingApp.models.enums.PriceTypeEnum;

public class PriceCardStringDTO {
    public TimeSlotStringDTO timeSlot;
    public double price;
    public PriceTypeEnum type;

    public Long accommodationId;

    public PriceCardStringDTO(TimeSlotStringDTO timeSlot, double price, PriceTypeEnum type) {
        this.timeSlot = timeSlot;
        this.price = price;
        this.type = type;
    }

    public TimeSlotStringDTO getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlotStringDTO timeSlot) {
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

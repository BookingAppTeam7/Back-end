package com.booking.BookingApp.models.dtos.reservations;

import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.enums.PriceTypeEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.sql.Time;
import java.util.Date;

public class ReservationPostDTO { //Reservation model without id and status (default --> PENDING)

    public  Long accommodationId;
    public String userId;
    public TimeSlot timeSlot;
    public Long numberOfGuests;
    public double price;
    public PriceTypeEnum priceType;

    public ReservationPostDTO( Long accommodationId, String userId, TimeSlot timeSlot, Long numberOfGuests, double price, PriceTypeEnum priceType) {

        this.accommodationId = accommodationId;
        this.userId=userId;
        this.timeSlot=timeSlot;
        this.numberOfGuests=numberOfGuests;
        this.price=price;
        this.priceType=priceType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PriceTypeEnum getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceTypeEnum priceType) {
        this.priceType = priceType;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Long numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Long getAccommodationId() {
        return accommodationId;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

}

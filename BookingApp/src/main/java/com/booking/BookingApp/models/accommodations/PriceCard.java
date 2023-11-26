package com.booking.BookingApp.models.accommodations;

import com.booking.BookingApp.models.enums.PriceTypeEnum;

import java.util.Date;

public class PriceCard {

    public Long id;
    //public Long accommodationId;

    public Date startDate;

    public Date endDate;

    public double price;
    public PriceTypeEnum type;

    public PriceCard(Long id, Date startDate, Date endDate, double price, PriceTypeEnum type) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

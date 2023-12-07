package com.booking.BookingApp.models.accommodations;

import com.booking.BookingApp.models.enums.PriceTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class PriceCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "accommodation_id")
//    public Accommodation accommodation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "time_slot_id")
    public TimeSlot timeSlot;
    public double price;
    @Enumerated(EnumType.STRING)
    public PriceTypeEnum type;

    public PriceCard(Long id, TimeSlot timeSlot, double price, PriceTypeEnum type) {
        this.id = id;
        this.timeSlot=timeSlot;
        this.price = price;
        this.type = type;
    }

    public PriceCard() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

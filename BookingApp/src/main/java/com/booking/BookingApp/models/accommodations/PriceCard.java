package com.booking.BookingApp.models.accommodations;

import com.booking.BookingApp.models.enums.PriceTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;

@Entity
@SQLDelete(sql = "UPDATE price_card SET deleted = true WHERE id = ?")
//@SQLDelete(sql = "UPDATE location SET deleted = true WHERE accommodation_id = ?")

@Where(clause="deleted=false")
public class PriceCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "time_slot_id")
    public TimeSlot timeSlot;
    public double price;
    @Enumerated(EnumType.STRING)
    public PriceTypeEnum type;

    @Column(name="deleted",columnDefinition = "boolean default false")
    private Boolean deleted;

    public PriceCard(Long id, TimeSlot timeSlot, double price, PriceTypeEnum type,Boolean deleted) {
        this.id = id;
        this.timeSlot=timeSlot;
        this.price = price;
        this.type = type;
        this.deleted=deleted;
    }

    public PriceCard(TimeSlot timeSlot, double price, PriceTypeEnum type,Boolean deleted) {
        this.timeSlot=timeSlot;
        this.price = price;
        this.type = type;
        this.deleted=deleted;
    }

    public PriceCard() {

    }

    public PriceCard(Long id, TimeSlot timeSlot, double price, PriceTypeEnum type) {
        this.id=id;
        this.timeSlot=timeSlot;
        this.price = price;
        this.type = type;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}

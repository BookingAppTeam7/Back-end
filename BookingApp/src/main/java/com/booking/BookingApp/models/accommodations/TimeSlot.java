package com.booking.BookingApp.models.accommodations;

import com.booking.BookingApp.models.enums.TimeSlotType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
@Entity

public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Date startDate;
    public Date endDate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @Enumerated(EnumType.STRING)
    public TimeSlotType type;

    public TimeSlot(Long id, Date startDate, Date endDate,TimeSlotType type) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type=type;
    }

    public TimeSlot() {

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public TimeSlotType getType() {
        return type;
    }

    public void setType(TimeSlotType type) {
        this.type = type;
    }
}

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
//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "accommodation_id")
//    private Accommodation accommodation;

//    @Enumerated(EnumType.STRING)
//    public TimeSlotType type;

    public TimeSlot(Long id, Date startDate, Date endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
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

}

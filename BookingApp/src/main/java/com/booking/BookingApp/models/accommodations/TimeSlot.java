package com.booking.BookingApp.models.accommodations;

import com.booking.BookingApp.models.enums.TimeSlotType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;
@Entity
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Date startDate;
    public Date endDate;

    @Column(name="deleted",columnDefinition = "boolean default false")
    private Boolean deleted;

    public TimeSlot(Long id, Date startDate, Date endDate,Boolean deleted) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deleted=deleted;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}

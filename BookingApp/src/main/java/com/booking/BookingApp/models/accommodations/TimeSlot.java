package com.booking.BookingApp.models.accommodations;

import com.booking.BookingApp.models.enums.TimeSlotType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;
@Entity
@SQLDelete(sql = "UPDATE time_slot SET deleted = true WHERE id = ?")
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

    public TimeSlot(Date startDate, Date endDate,Boolean deleted) {
        this.startDate=startDate;
        this.endDate=endDate;
        this.deleted=deleted;
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

    public boolean overlapsWith(TimeSlot other) {
        return (this.startDate.before(other.endDate) || this.startDate.equals(other.endDate)) && (this.endDate.after(other.startDate)|| this.endDate.equals(other.startDate));
    }

    // Merge two overlapping time slots into a new time slot
    public TimeSlot mergeWith(TimeSlot other) {
        Date mergedStart = this.startDate.before(other.startDate) ? this.startDate : other.startDate;
        Date mergedEnd = this.endDate.after(other.endDate) ? this.endDate : other.endDate;
        return new TimeSlot(mergedStart, mergedEnd, false);
    }
}

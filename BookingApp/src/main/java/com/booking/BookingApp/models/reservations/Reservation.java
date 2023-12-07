package com.booking.BookingApp.models.reservations;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Table(name="reservations")
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    public Accommodation accommodation;

    public Long userId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "time_slot_id")
    public TimeSlot timeSlot;
    @Enumerated(EnumType.STRING)
    public ReservationStatusEnum status;

    public Reservation(){}

    public Reservation(Long id,Long userId, TimeSlot timeSlot,ReservationStatusEnum status, Accommodation accommodation) {
        this.id = id;
        this.userId=userId;
        this.timeSlot=timeSlot;
        this.status=status;
        this.accommodation=accommodation;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public Long getId() {
        return id;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public ReservationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ReservationStatusEnum status) {
        this.status = status;
    }
}

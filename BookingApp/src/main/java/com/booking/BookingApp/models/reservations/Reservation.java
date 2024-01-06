package com.booking.BookingApp.models.reservations;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.enums.PriceTypeEnum;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Table(name="reservations")
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    public Accommodation accommodation;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    //public Long userId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "time_slot_id")
    public TimeSlot timeSlot;
    @Enumerated(EnumType.STRING)
    public ReservationStatusEnum status;

    public Long numberOfGuests;

    public double price;
    @Enumerated(EnumType.STRING)
    public PriceTypeEnum priceType;


    public Reservation(){}

   // public Reservation(Long id, TimeSlot timeSlot,ReservationStatusEnum status, Accommodation accommodation, Long numberOfGuests, User user) {
  //      this.id = id;
  //      this.timeSlot=timeSlot;
  //      this.status=status;
   //     this.accommodation=accommodation;
  //      this.numberOfGuests=numberOfGuests;
  //      this.user=user;
  //  }

    public Reservation(Long id, Accommodation accommodation, User user, TimeSlot timeSlot, ReservationStatusEnum status, Long numberOfGuests, double price, PriceTypeEnum priceType) {
        this.id = id;
        this.accommodation = accommodation;
        this.user = user;
        this.timeSlot = timeSlot;
        this.status = status;
        this.numberOfGuests = numberOfGuests;
        this.price = price;
        this.priceType = priceType;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Long numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
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


   // public Long getUserId() {
    //    return userId;
    //}

    //public void setUserId(Long userId) {
    //    this.userId = userId;
   // }

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

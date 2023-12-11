package com.booking.BookingApp.models.users;

import com.booking.BookingApp.models.enums.NotificationTypeEnum;
import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.enums.StatusEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.Map;
@Table(name="users")
@SQLDelete(sql
        = "UPDATE users "
        + "SET deleted = true "
        + "WHERE username = ?")
@Where(clause = "deleted = false")
@Entity
public class User {
    //@Id
    //public Long id;

    public String firstName;
    public String lastName;
    @Id
    public String username;
    public String password;
    public RoleEnum role;

    public String address;
    public String phoneNumber;

    public StatusEnum status;
    @Column(name = "deleted",columnDefinition = "boolean default false")
    private Boolean deleted;

    @Column(nullable = true,columnDefinition = "boolean default false")
    public Boolean reservationRequestNotification=false;
    @Column(nullable = true,columnDefinition = "boolean default false")
    public Boolean reservationCancellationNotification=false;
    @Column(nullable = true,columnDefinition = "boolean default false")
    public Boolean ownerRatingNotification=false;
    @Column(nullable = true,columnDefinition = "boolean default false")
    public Boolean accommodationRatingNotification=false;
    //guest
    @Column(nullable = true,columnDefinition = "boolean default false")
    public Boolean ownerRepliedToRequestNotification=false;


    @Column(name = "token")
    public String token;

    //@OneToMany(cascade=CascadeType.ALL)
    //public List<Reservation> reservations;

    public User(String firstName, String lastName, String username, String password, RoleEnum role, String address, String phoneNumber, StatusEnum status, Boolean reservationRequestNotification, Boolean reservationCancellationNotification, Boolean ownerRatingNotification,
                Boolean accommodationRatingNotification, Boolean ownerRepliedToRequestNotification,String token,Boolean deleted) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.reservationRequestNotification = reservationRequestNotification;
        this.reservationCancellationNotification = reservationCancellationNotification;
        this.ownerRatingNotification = ownerRatingNotification;
        this.accommodationRatingNotification = accommodationRatingNotification;
        this.ownerRepliedToRequestNotification = ownerRepliedToRequestNotification;
        //this.reservations=reservations;
        this.token=token;
        this.deleted=deleted;

    }

    public User() {

    }

   // public Long getId() {
       // return id;
    //}


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }



    public RoleEnum getRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public Boolean getReservationRequestNotification() {
        return reservationRequestNotification;
    }

    public void setReservationRequestNotification(Boolean reservationRequestNotification) {
        this.reservationRequestNotification = reservationRequestNotification;
    }

    public Boolean getReservationCancellationNotification() {
        return reservationCancellationNotification;
    }

    public void setReservationCancellationNotification(Boolean reservationCancellationNotification) {
        this.reservationCancellationNotification = reservationCancellationNotification;
    }

    public Boolean getOwnerRatingNotification() {
        return ownerRatingNotification;
    }

    public void setOwnerRatingNotification(Boolean ownerRatingNotification) {
        this.ownerRatingNotification = ownerRatingNotification;
    }

    public Boolean getAccommodationRatingNotification() {
        return accommodationRatingNotification;
    }

    public void setAccommodationRatingNotification(Boolean accommodationRatingNotification) {
        this.accommodationRatingNotification = accommodationRatingNotification;
    }

    public Boolean getOwnerRepliedToRequestNotification() {
        return ownerRepliedToRequestNotification;
    }

    public void setOwnerRepliedToRequestNotification(Boolean ownerRepliedToRequestNotification) {
        this.ownerRepliedToRequestNotification = ownerRepliedToRequestNotification;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }


}

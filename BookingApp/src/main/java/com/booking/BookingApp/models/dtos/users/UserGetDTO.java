package com.booking.BookingApp.models.dtos.users;

import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.enums.StatusEnum;

public class UserGetDTO {  //User model without password

    public String firstName;
    public String lastName;
    public String username;
    public RoleEnum role;

    public String address;
    public String phoneNumber;

    public StatusEnum status;
    public Boolean deleted;
    public Boolean reservationRequestNotification;
    public Boolean reservationCancellationNotification;

    public Boolean ownerRatingNotification;

    public Boolean accommodationRatingNotification;

    //guest
    public Boolean ownerRepliedToRequestNotification;
    public String token;

    public UserGetDTO(String firstName, String lastName, String username, RoleEnum role, String address, String phoneNumber,
                      StatusEnum status, Boolean deleted, Boolean reservationRequestNotification, Boolean reservationCancellationNotification,
                      Boolean ownerRatingNotification, Boolean accommodationRatingNotification, Boolean ownerRepliedToRequestNotification,
                      String token) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.deleted = deleted;
        this.reservationRequestNotification = reservationRequestNotification;
        this.reservationCancellationNotification = reservationCancellationNotification;
        this.ownerRatingNotification = ownerRatingNotification;
        this.accommodationRatingNotification = accommodationRatingNotification;
        this.ownerRepliedToRequestNotification = ownerRepliedToRequestNotification;
        this.token = token;
    }

    public UserGetDTO() {

    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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


    public RoleEnum getRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
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

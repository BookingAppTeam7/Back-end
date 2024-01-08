package com.booking.BookingApp.models.dtos.users;

import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;

public class UserGetDTO {  //User model without password

    public String firstName;
    public String lastName;
    public String username;
    public RoleEnum role;

    public String address;
    public String phoneNumber;

    public StatusEnum status;
    public Boolean deleted;
    public String token;

    public Boolean reservationRequestNotification;

    public Boolean reservationCancellationNotification;

    public Boolean ownerRatingNotification;

    public Boolean accommodationRatingNotification;
    //guest

    public Boolean ownerRepliedToRequestNotification;

    @Transient
    public String jwt;
//    public UserGetDTO( String firstName, String lastName, String username, RoleEnum role, String address, String phoneNumber,StatusEnum status, String token,String jwt) {
//       // this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.username = username;
//        this.role = role;
//        this.address = address;
//        this.phoneNumber = phoneNumber;
//        this.status=status;
//        this.token=token;
//        this.jwt=jwt;
//    }
    public String favouriteAccommodations;

    public UserGetDTO(String firstName, String lastName, String username, RoleEnum role, String address, String phoneNumber, StatusEnum status, Boolean deleted, Boolean reservationRequestNotification, Boolean reservationCancellationNotification, Boolean ownerRatingNotification, Boolean accommodationRatingNotification,
                      Boolean ownerRepliedToRequestNotification,String token, String jwt, String favouriteAccommodations) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.deleted=deleted;
        this.token = token;
        this.reservationRequestNotification = reservationRequestNotification;
        this.reservationCancellationNotification = reservationCancellationNotification;
        this.ownerRatingNotification = ownerRatingNotification;
        this.accommodationRatingNotification = accommodationRatingNotification;
        this.ownerRepliedToRequestNotification = ownerRepliedToRequestNotification;

        this.jwt = jwt;
        this.favouriteAccommodations=favouriteAccommodations;
    }

    public UserGetDTO() {

    }


    public String getFavouriteAccommodations() {
        return favouriteAccommodations;
    }

    public void setFavouriteAccommodations(String favouriteAccommodations) {
        this.favouriteAccommodations = favouriteAccommodations;
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

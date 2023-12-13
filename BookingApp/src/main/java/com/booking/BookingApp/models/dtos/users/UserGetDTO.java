package com.booking.BookingApp.models.dtos.users;

import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.enums.StatusEnum;
import jakarta.persistence.Transient;

public class UserGetDTO {  //User model without password

    public String firstName;
    public String lastName;
    public String username;
    public RoleEnum role;

    public String address;
    public String phoneNumber;

    public StatusEnum status;
    public String token;

    @Transient
    public String jwt;
    public UserGetDTO( String firstName, String lastName, String username, RoleEnum role, String address, String phoneNumber,StatusEnum status, String token,String jwt) {
       // this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status=status;
        this.token=token;
        this.jwt=jwt;
    }

    public UserGetDTO() {

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

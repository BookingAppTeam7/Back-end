package com.booking.BookingApp.models.dtos;

import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.enums.StatusEnum;

public class UserGetDTO {  //user without password
    public Long id;
    public String firstName;
    public String lastName;
    public String username;
    public RoleEnum role;

    public String address;
    public String phoneNumber;

    public StatusEnum status;

    public UserGetDTO(Long id, String firstName, String lastName, String username, RoleEnum role, String address, String phoneNumber,StatusEnum status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status=status;
    }

    public UserGetDTO() {

    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
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

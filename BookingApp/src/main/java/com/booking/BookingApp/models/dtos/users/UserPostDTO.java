package com.booking.BookingApp.models.dtos.users;

import com.booking.BookingApp.models.enums.RoleEnum;

public class UserPostDTO {  //User model without id and status (created user is default DEACTIVE)
    public String firstName;
    public String lastName;
    public String username;
    public String password;
    public String passwordConfirmation;
    public RoleEnum role;

    public String address;
    public String phoneNumber;

    public UserPostDTO(String firstName, String lastName, String username, String password, String passwordConfirmation,RoleEnum role, String address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.passwordConfirmation=passwordConfirmation;
        this.role = role;
        this.address = address;
        this.phoneNumber = phoneNumber;
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

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}

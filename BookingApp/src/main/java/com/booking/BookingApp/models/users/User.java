package com.booking.BookingApp.models.users;

import com.booking.BookingApp.models.enums.NotificationTypeEnum;
import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.enums.StatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Map;


public class User {

    public Long id;
    public String firstName;
    public String lastName;
    public String username;
    public String password;
    public RoleEnum role;

    public String address;
    public String phoneNumber;

    public StatusEnum status;

    public Map<NotificationTypeEnum,Boolean> notificationSettings;

    public User(Long id, String firstName, String lastName, String username, String password, RoleEnum role, String address, String phoneNumber,StatusEnum status,Map<NotificationTypeEnum,Boolean> notificationSettings) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status=status;
        this.notificationSettings=notificationSettings;
    }

    public User() {

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

    public Map<NotificationTypeEnum, Boolean> getNotificationSettings() {
        return notificationSettings;
    }

    public void setNotificationSettings(Map<NotificationTypeEnum, Boolean> notificationSettings) {
        this.notificationSettings = notificationSettings;
    }
}

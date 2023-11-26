package com.booking.BookingApp.models.users;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.enums.NotificationTypeEnum;
import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.enums.StatusEnum;

import java.util.List;
import java.util.Map;

public class Guest extends User{
    public List<Accommodation> favoriteAccomodations;

    public Guest(Long id, String firstName, String lastName, String username, String password,
                 RoleEnum role, String address, String phoneNumber, StatusEnum status,
                 Map<NotificationTypeEnum, Boolean> notificationSettings, List<Accommodation> favoriteAccomodations) {
        super(id, firstName, lastName, username, password, role, address, phoneNumber, status, notificationSettings);
        this.favoriteAccomodations = favoriteAccomodations;
    }

    public List<Accommodation> getFavoriteAccomodations() {
        return favoriteAccomodations;
    }

    public void setFavoriteAccomodations(List<Accommodation> favoriteAccomodations) {
        this.favoriteAccomodations = favoriteAccomodations;
    }
}

package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.users.Notification;

import java.util.List;
import java.util.Optional;

public interface INotificationRepository {
    List<Notification> findAll();

    Optional<Notification> findById(Long id);

    Optional<Notification> save(Notification createdNotification);

    Notification saveAndFlush(Notification result);

    void deleteById(Long id);
}

package com.booking.BookingApp.services;

import com.booking.BookingApp.models.dtos.users.NotificationPostDTO;
import com.booking.BookingApp.models.dtos.users.NotificationPutDTO;
import com.booking.BookingApp.models.users.Notification;

import java.util.List;
import java.util.Optional;

public interface INotificationService {
    List<Notification> findAll();

    Optional<Notification> findById(Long id);

    Optional<Notification> create(NotificationPostDTO newNotification) throws Exception;
    Notification update(NotificationPutDTO updatedNotification, Long  id) throws  Exception;
    void delete(Long id);
    List<Notification> findByUserId(String userId);
}

package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.users.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface INotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByUserId(String userId);
}

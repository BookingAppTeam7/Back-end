package com.booking.BookingApp.services;

import com.booking.BookingApp.models.dtos.users.NotificationPostDTO;
import com.booking.BookingApp.models.dtos.users.NotificationPutDTO;
import com.booking.BookingApp.models.users.Notification;
import com.booking.BookingApp.repositories.INotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    public INotificationRepository notificationRepository;
    private static AtomicLong counter=new AtomicLong();
    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> findById(Long id) {
        Optional<Notification> n=notificationRepository.findById(id);
        if(n.isPresent()) {
            return Optional.of(n.get());
        }
        return null;
    }

    @Override
    public Optional<Notification> create(NotificationPostDTO newNotification) throws Exception {
        Long newId= (Long) counter.incrementAndGet();
        Notification createdNotification=new Notification(newId,newNotification.userId,newNotification.type, newNotification.content, LocalDateTime.now());
        return notificationRepository.save(createdNotification);
    }

    @Override
    public Notification update(NotificationPutDTO updatedNotification, Long id) throws Exception {
        Notification result=new Notification(id, updatedNotification.userId, updatedNotification.type, updatedNotification.content, updatedNotification.dateTime);
        return notificationRepository.saveAndFlush(result);
    }

    @Override
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
}

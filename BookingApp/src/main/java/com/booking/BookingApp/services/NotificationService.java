package com.booking.BookingApp.services;

import com.booking.BookingApp.models.dtos.users.NotificationPostDTO;
import com.booking.BookingApp.models.dtos.users.NotificationPutDTO;
import com.booking.BookingApp.models.users.Notification;
import com.booking.BookingApp.repositories.INotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
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
        Notification createdNotification=new Notification(newNotification.userId,newNotification.type, newNotification.content, LocalDateTime.now(),false);
        Notification result=notificationRepository.save(createdNotification);
        return Optional.of(result);
    }

    @Override
    public Notification update(NotificationPutDTO updatedNotification, Long id) throws Exception {
        Notification result=new Notification(id, updatedNotification.userId, updatedNotification.type, updatedNotification.content, updatedNotification.dateTime, updatedNotification.read);
        return notificationRepository.saveAndFlush(result);
    }

    @Override
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public List<Notification> findByUserId(String userId) {
        List<Notification>result=notificationRepository.findByUserId(userId);
        Collections.sort(result, Comparator.comparing(Notification::getDateTime).reversed());
        return result;
    }

    @Override
    public void read(Long id) throws Exception {
        Notification notification=findById(id).orElseThrow(()->new Exception("Notification not found"));
        notification.setRead(true);
        notificationRepository.saveAndFlush(notification);
    }
}

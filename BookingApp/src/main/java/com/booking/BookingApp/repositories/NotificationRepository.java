package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.users.Notification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class NotificationRepository implements INotificationRepository {
    private List<Notification> notifications=new ArrayList<>();
    private static AtomicLong counter=new AtomicLong();
    @Override
    public List<Notification> findAll() {
        return this.notifications;
    }

    public Optional<Notification> getById(Long id){
        return notifications.stream().filter(notification-> notification.getId()== id).findFirst();
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notifications.stream().filter(notification -> notification.getId() == id).findFirst();
    }

    @Override
    public Optional<Notification> save(Notification newNotification) {
        this.notifications.add(newNotification);
        return getById(newNotification.getId());
    }

    @Override
    public Notification saveAndFlush(Notification updatedNotification) {
        Optional<Notification> optionalNotification=getById(updatedNotification.id);

        if (optionalNotification.isPresent()) {
            Notification notification= optionalNotification.get();
            notification.setUserId(updatedNotification.userId);
            notification.setContent(updatedNotification.content);
            notification.setDateTime(updatedNotification.dateTime);
            notification.setType(updatedNotification.type);

            return notification;
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        Optional<Notification> notification=getById(id);
        if(notification.isPresent()){
            Notification notificationToDelete = notification.get();
            notifications.remove(notificationToDelete);
        }
    }

}

package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.users.User;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TimeSlotRepository implements  ITimeSlotRepository {
    private List<TimeSlot> timeSlots=new ArrayList<>();
    private static AtomicLong counter=new AtomicLong();
    @Override
    public List<TimeSlot> findAll() {
        return this.timeSlots;
    }

    public Optional<TimeSlot> getById(Long id){
        return timeSlots.stream().filter(user -> user.getId() == id).findFirst();
    }

    @Override
    public Optional<TimeSlot> findById(Long id) {
        return timeSlots.stream().filter(user -> user.getId() == id).findFirst();
    }

    @Override
    public Optional<TimeSlot> save(TimeSlot newTimeSlot) {
        this.timeSlots.add(newTimeSlot);
        return getById(newTimeSlot.getId());
    }

    @Override
    public TimeSlot saveAndFlush(TimeSlot updatedTimeSlot) {
        Optional<TimeSlot> optionalTimeSlot=getById(updatedTimeSlot.id);

        if (optionalTimeSlot.isPresent()) {
            TimeSlot timeSlot = optionalTimeSlot.get();
            timeSlot.setStartDate(updatedTimeSlot.getStartDate());
            timeSlot.setEndDate(updatedTimeSlot.getEndDate());
            return timeSlot;
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        Optional<TimeSlot> timeSlot=getById(id);
        if(timeSlot.isPresent()){
            TimeSlot timeSlotToDelete = timeSlot.get();
            timeSlots.remove(timeSlotToDelete);
        }
    }
}

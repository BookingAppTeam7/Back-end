package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.TimeSlot;
import com.booking.BookingApp.models.dtos.accommodations.TimeSlotPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.TimeSlotPutDTO;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.models.enums.NotificationTypeEnum;
import com.booking.BookingApp.models.enums.StatusEnum;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.ITimeSlotRepository;
import com.booking.BookingApp.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TimeSlotService implements  ITimeSlotService{

    @Autowired
    public ITimeSlotRepository timeSlotRepository;
    private static AtomicLong counter=new AtomicLong();

    @Override
    public List<TimeSlot> findAll() {
        List<TimeSlot> result=timeSlotRepository.findAll();
        return result;
    }

    @Override
    public Optional<TimeSlot> findById(Long id) {
        Optional<TimeSlot> t=timeSlotRepository.findById(id);
        if(t.isPresent()) {
            return t;
        }
        return null;
    }

    @Override
    public Optional<TimeSlot> create(TimeSlotPostDTO newTimeSlot) throws Exception {
        Long newId= (Long) counter.incrementAndGet();
        TimeSlot createdTimeSlot=new TimeSlot(newId,newTimeSlot.startDate,newTimeSlot.endDate);
        return timeSlotRepository.save(createdTimeSlot);
    }

    @Override
    public TimeSlot update(TimeSlotPutDTO updatedTimeSlot, Long id) throws Exception {
        TimeSlot result=new TimeSlot(id,updatedTimeSlot.startDate,updatedTimeSlot.endDate)
;        return timeSlotRepository.saveAndFlush(result);
    }

    @Override
    public void delete(Long id) {
        timeSlotRepository.deleteById(id);
    }
}

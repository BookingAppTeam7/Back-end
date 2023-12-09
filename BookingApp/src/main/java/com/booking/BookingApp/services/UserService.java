package com.booking.BookingApp.services;

import com.booking.BookingApp.models.enums.NotificationTypeEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.models.enums.StatusEnum;
import com.booking.BookingApp.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService implements IUserService{
    @Autowired
    public IUserRepository userRepository;
    private static AtomicLong counter=new AtomicLong();

    @Override
    public List<User> findAll() {
        //List<User> result=userRepository.findAll();
        return userRepository.findAll();
       //List<UserGetDTO>resultDTO=new ArrayList<>();

        //for(User u:result){
        //    resultDTO.add(new UserGetDTO(u.getFirstName(),u.getLastName(),u.getUsername(),u.getRole(),u.getAddress(),u.getPhoneNumber(),u.getStatus(),u.getToken()));
        //}
        //return resultDTO;
    }

    @Override
    public Optional<User> findById(String username) {
        Optional<User> u=userRepository.findById(username);
        if(u.isPresent()) {
            return u;
        }
        return null;
    }

    @Override
    public Optional<User> create(UserPostDTO newUser) throws Exception {
        //Long newId= (Long) counter.incrementAndGet();
        Map<NotificationTypeEnum,Boolean>notificationSettings=null;
        String token = UUID.randomUUID().toString();
        User createdUser=new User(newUser.firstName, newUser.lastName,newUser.username, newUser.password, newUser.role,newUser.address,newUser.phoneNumber, StatusEnum.DEACTIVE,newUser.reservationRequestNotification,
                newUser.reservationCancellationNotification,newUser.ownerRatingNotification,newUser.accommodationRatingNotification,newUser.ownerRepliedToRequestNotification, token, newUser.deleted);
        return Optional.of(userRepository.save(createdUser));
    }

    @Override
    public User update(UserPutDTO updatedUser, String username) throws Exception {
        User result=new User(updatedUser.firstName, updatedUser.lastName,username, updatedUser.password, updatedUser.role,updatedUser.address,updatedUser.phoneNumber, updatedUser.status,
                updatedUser.reservationRequestNotification,updatedUser.reservationCancellationNotification,updatedUser.ownerRatingNotification,
                updatedUser.accommodationRatingNotification, updatedUser.ownerRepliedToRequestNotification, updatedUser.token, updatedUser.deleted);
        return userRepository.saveAndFlush(result);
    }

    @Override
    public void delete(String username) {
        userRepository.deleteById(username);
    }

    @Override
    public Optional<User> findByToken(String token){
        return userRepository.findByToken(token);
    }
    @Override
    public Optional<User> save(User user){
        return Optional.of(userRepository.save(user));
    }
}

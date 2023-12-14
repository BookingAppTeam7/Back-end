package com.booking.BookingApp.services;


import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;

public interface IUserValidatorService {
    void validatePost(UserPostDTO user);
    void validatePut(UserPutDTO user, String username);

}

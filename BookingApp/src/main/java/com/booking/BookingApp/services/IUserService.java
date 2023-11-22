package com.booking.BookingApp.services;

import com.booking.BookingApp.models.User;
import com.booking.BookingApp.models.dtos.UserGetDTO;
import com.booking.BookingApp.models.dtos.UserPostDTO;
import com.booking.BookingApp.models.dtos.UserPutDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<UserGetDTO> findAll();

    Optional<UserGetDTO> findById(Long id);

    Optional<User> create(UserPostDTO newUser) throws Exception;
    User update(UserPutDTO updatedUser,Long  id) throws  Exception;
    void delete(Long id);
}

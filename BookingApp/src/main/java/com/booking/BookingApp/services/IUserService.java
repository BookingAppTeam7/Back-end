package com.booking.BookingApp.services;

import com.booking.BookingApp.models.User;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<UserGetDTO> findAll();

    Optional<UserGetDTO> findById(Long id);

    Optional<User> create(UserPostDTO newUser) throws Exception;
    User update(UserPutDTO updatedUser,Long  id) throws  Exception;
    void delete(Long id);
}

package com.booking.BookingApp.services;

import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<UserGetDTO> findAll();

    Optional<UserGetDTO> findById(String username);

    Optional<User> create(UserPostDTO newUser) throws Exception;
    User update(UserPutDTO updatedUser,String username) throws  Exception;
    void delete(String username) throws Exception;
    Optional<User> findByToken(String token);
    Optional<User> save(User user);
    User findUserById(String username);
    void activateUser(String token) throws Exception;

    List<User> findByRole(RoleEnum role);
    void addFavouriteAccommodation(String username, Long accId) throws Exception;
    void removeFavouriteAccommodation(String username, Long accId) throws Exception;
}

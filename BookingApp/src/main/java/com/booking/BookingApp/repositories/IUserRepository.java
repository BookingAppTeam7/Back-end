package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> save(User createdUser);

    User saveAndFlush(User result);

    void deleteById(Long id);
}

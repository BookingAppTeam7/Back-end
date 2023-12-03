package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User,String> {

    //List<User> findAll();

   //Optional<User> findById(Long id);

   // Optional<User> save(User createdUser);

   // User saveAndFlush(User result);

   // void deleteById(Long id);
}

package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.enums.StatusEnum;
import com.booking.BookingApp.models.users.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface IUserRepository extends JpaRepository<User,String> {

    //List<User> findAll();

   //Optional<User> findById(Long id);

   // Optional<User> save(User createdUser);

   // User saveAndFlush(User result);

   // void deleteById(Long id);
    //Optional<User> findByToken(String token);
    List<User> findByRole(RoleEnum role);
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = :newStatus WHERE u.username = :userId")
    int updateStatus(@Param("userId") String userId, @Param("newStatus") StatusEnum newStatus);
}

package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IUserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;
import java.util.regex.*;

@Service
public class UserValidatorService implements IUserValidatorService{

    String usernamePattern = ".*@gmail\\.com";
    String phoneNumberPattern = "\\d{10}";
    @Autowired
    public IUserRepository userRepository;

    @Override
    public void validatePost(UserPostDTO user) {
        if (user == null) {
            throw new IllegalArgumentException("User data are not valid");
        }

        if (StringUtils.isEmpty(user.username)) {
            throw new IllegalArgumentException("User  username cannot be empty");
        }

        if (StringUtils.isEmpty(user.firstName)) {
            throw new IllegalArgumentException("User firstName cannot be empty");
        }
        if (StringUtils.isEmpty(user.lastName)) {
            throw new IllegalArgumentException("User lastName cannot be empty");
        }
        if (StringUtils.isEmpty(user.password)) {
            throw new IllegalArgumentException("User password cannot be empty");
        }
        if (StringUtils.isEmpty(user.passwordConfirmation)) {
            throw new IllegalArgumentException("User password Confirmation cannot be empty");
        }
        if (StringUtils.isEmpty(user.address)) {
            throw new IllegalArgumentException("User address cannot be empty");
        }
        if(!Pattern.matches(usernamePattern, user.username) || !Pattern.matches(phoneNumberPattern, user.phoneNumber)){
            throw new IllegalArgumentException("User username or phone number are not valid ");
        }
        if(user.role ==null){
            throw new IllegalArgumentException("User role cannot be null ");
        }
        if(!user.password.equals(user.passwordConfirmation)){
            throw new IllegalArgumentException("User password and confirmation Password are not equals");
        }

    }

    @Override
    public void validatePut(UserPutDTO user, String username) {
        Optional<User> userToUpdate=userRepository.findById(username);
        System.out.println(username);
        if(!userToUpdate.isPresent()){
            throw new IllegalArgumentException("Cannot change user with this id - not found!");
        }

        if (user == null) {
            throw new IllegalArgumentException("User data are not valid");
        }

        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("User  username cannot be empty");
        }

        if (StringUtils.isEmpty(user.firstName)) {
            throw new IllegalArgumentException("User firstName cannot be empty");
        }
        if (StringUtils.isEmpty(user.lastName)) {
            throw new IllegalArgumentException("User lastName cannot be empty");
        }
        if (StringUtils.isEmpty(user.password)) {
            throw new IllegalArgumentException("User password cannot be empty");
        }
        if (StringUtils.isEmpty(user.address)) {
            throw new IllegalArgumentException("User address cannot be empty");
        }
        if(!Pattern.matches(usernamePattern, username) || !Pattern.matches(phoneNumberPattern, user.phoneNumber)){
            throw new IllegalArgumentException("User username or phone number are not valid ");
        }
//        if(user.role ==null){
//            throw new IllegalArgumentException("User role cannot be null ");
//        }
    }
}

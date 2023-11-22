package com.booking.BookingApp.services;

import com.booking.BookingApp.models.User;
import com.booking.BookingApp.models.dtos.UserGetDTO;
import com.booking.BookingApp.models.dtos.UserPostDTO;
import com.booking.BookingApp.models.dtos.UserPutDTO;
import com.booking.BookingApp.models.enums.StatusEnum;
import com.booking.BookingApp.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService implements IUserService{
    @Autowired
    public IUserRepository userRepository;
    private static AtomicLong counter=new AtomicLong();

    @Override
    public List<UserGetDTO> findAll() {
        List<User> result=userRepository.findAll();
        List<UserGetDTO>resultDTO=new ArrayList<>();

        for(User u:result){
            resultDTO.add(new UserGetDTO(u.getId(),u.getFirstName(),u.getLastName(),u.getUsername(),u.getRole(),u.getAddress(),u.getPhoneNumber(),u.getStatus()));
        }
        return resultDTO;
    }

    @Override
    public Optional<UserGetDTO> findById(Long id) {
        Optional<User> u=userRepository.findById(id);
        if(u.isPresent()) {
            UserGetDTO userDTO = new UserGetDTO(u.get().getId(), u.get().getFirstName(), u.get().getLastName(), u.get().getUsername(), u.get().getRole(), u.get().getAddress(), u.get().getPhoneNumber(), u.get().getStatus());
            return Optional.of(userDTO);
        }
        return null;
    }

    @Override
    public Optional<User> create(UserPostDTO newUser) throws Exception {
        Long newId= (Long) counter.incrementAndGet();
        User createdUser=new User(newId,newUser.firstName, newUser.lastName,newUser.username, newUser.password, newUser.role,newUser.address,newUser.address, StatusEnum.ACTIVE);
        return userRepository.save(createdUser);
    }

    @Override
    public User update(UserPutDTO updatedUser, Long id) throws Exception {
        User result=new User(id,updatedUser.firstName, updatedUser.lastName,updatedUser.username, updatedUser.password, updatedUser.role,updatedUser.address,updatedUser.address, updatedUser.status);
        return userRepository.saveAndFlush(result);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);

    }
}

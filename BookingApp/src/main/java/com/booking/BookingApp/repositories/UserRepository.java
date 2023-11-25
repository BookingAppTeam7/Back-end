package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.users.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository implements IUserRepository{

    private List<User> users=new ArrayList<>();
    private static AtomicLong counter=new AtomicLong();
    @Override
    public List<User> findAll() {
        return this.users;
    }

    public Optional<User> getById(Long id){
        return users.stream().filter(user -> user.getId() == id).findFirst();
    }

    @Override
    public Optional<User> findById(Long id) {
        return users.stream().filter(user -> user.getId() == id).findFirst();
    }

    @Override
    public Optional<User> save(User newUser) {
        this.users.add(newUser);
        return getById(newUser.getId());
    }

    @Override
    public User saveAndFlush(User updatedUser) {
        Optional<User> optionalUser=getById(updatedUser.id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            user.setRole(updatedUser.getRole());
            user.setAddress(updatedUser.getAddress());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setStatus(updatedUser.getStatus());

            return user;
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        Optional<User> user=getById(id);
        if(user.isPresent()){
            User userToDelete = user.get();
            users.remove(userToDelete);
        }
    }

}

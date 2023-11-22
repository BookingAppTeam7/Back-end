package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.User;
import com.booking.BookingApp.models.dtos.UserGetDTO;
import com.booking.BookingApp.models.dtos.UserPostDTO;
import com.booking.BookingApp.models.dtos.UserPutDTO;
import com.booking.BookingApp.services.IUserService;
import com.booking.BookingApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserGetDTO>> findAll(){
        List<UserGetDTO> users=userService.findAll();
        return new ResponseEntity<List<UserGetDTO>>(users,HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    public Optional<UserGetDTO> findById(@PathVariable Long id){
        return userService.findById(id);
    }


    @PostMapping
    public Optional<User> create(@RequestBody UserPostDTO newUser) throws Exception {
        return userService.create(newUser);

    }

    @PutMapping(value="/{id}")
    public User update(@RequestBody UserPutDTO user, @PathVariable Long id) throws Exception {
        return userService.update(user,id);

    }
    @DeleteMapping(value="/{id}")
    public ResponseEntity<User> delete(@PathVariable Long id){
        userService.delete(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
}

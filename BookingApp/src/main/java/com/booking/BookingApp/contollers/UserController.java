package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.User;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.services.IUserService;
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
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserGetDTO> findById(@PathVariable Long id){
        Optional<UserGetDTO> result=userService.findById(id);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result.get(),HttpStatus.OK );
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody UserPostDTO newUser) throws Exception {
        Optional<User> result=userService.create(newUser);
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);

    }

    @PutMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> update(@RequestBody UserPutDTO user, @PathVariable Long id) throws Exception {
        User result=userService.update(user,id);
        return new ResponseEntity<>(result,HttpStatus.OK);

    }
    @DeleteMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> delete(@PathVariable Long id){
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
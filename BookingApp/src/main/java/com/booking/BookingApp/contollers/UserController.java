package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.users.User;
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
    @CrossOrigin(origins = "http://localhost:4200") // Postavite odgovarajuću putanju do vaše Angular aplikacije
    public ResponseEntity<List<UserGetDTO>> findAll(){
        List<UserGetDTO> users=userService.findAll();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping(value="/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserGetDTO> findById(@PathVariable String username){
        Optional<UserGetDTO> result=userService.findById(username);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result.get(),HttpStatus.OK );
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200") // Postavite odgovarajuću putanju do vaše Angular aplikacije
    public ResponseEntity<User> create(@RequestBody UserPostDTO newUser) throws Exception {
        Optional<User> result=userService.create(newUser);
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);

    }

    @PutMapping(value="/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<User> update(@RequestBody UserPutDTO user, @PathVariable String username) throws Exception {
        User result=userService.update(user,username);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result,HttpStatus.OK);

    }
    @DeleteMapping(value="/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<User> delete(@PathVariable String username){
        userService.delete(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
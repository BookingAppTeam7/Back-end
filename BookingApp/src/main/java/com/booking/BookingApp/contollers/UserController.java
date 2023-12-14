package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.enums.StatusEnum;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.services.EmailService;
import com.booking.BookingApp.services.IUserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;


import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private EmailService emailService;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200") // Postavite odgovarajuću putanju do vaše Angular aplikacije
   //  @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> findAll(){
        List<User> users=userService.findAll();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping(value="/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public Optional<User> findById(@PathVariable String username){
        return userService.findById(username);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200") // Postavite odgovarajuću putanju do vaše Angular aplikacije
    public ResponseEntity<User> create(@RequestBody UserPostDTO newUser) throws Exception {
        Optional<User> optionalUser=userService.findById(newUser.username);
        if(optionalUser!=null){
            if(optionalUser.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//zato sto username mora biti jedinstven
            }
        }
        Optional<User> result=userService.create(newUser);
        if(result.isPresent()){
            System.out.println("NASAO SALJEM MEJL");
            User createdUser=result.get();
            //EmailService emailService=new EmailService();
            emailService.send(createdUser.username,generateActivationEmailBody(createdUser.firstName+" "+createdUser.lastName,createdUser.token), "BookingApp - Confirm registration");
        }
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
    @GetMapping("/activate/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token) throws Exception {
        Optional<User> optionalUser = userService.findByToken(token);
        if (optionalUser.isPresent()) {
            User user=optionalUser.get();
            user.setStatus(StatusEnum.ACTIVE);
            UserPutDTO userPutDTO=new UserPutDTO(user.firstName,user.lastName,user.username,user.password,user.role,user.address,
                    user.phoneNumber,user.status,user.reservationRequestNotification,user.reservationCancellationNotification,user.ownerRatingNotification,
                    user.accommodationRatingNotification,user.ownerRepliedToRequestNotification,user.token,user.getDeleted());
            userService.update(userPutDTO,user.username);
            return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid activation token", HttpStatus.NOT_FOUND);
        }
    }
    public String generateActivationEmailBody(String userName, String activationLink) {
        String fullActivationLink = "http://localhost:4200/activate/" + activationLink;

        return "<p>Dear <strong>" + userName + "</strong>,</p>\n" +
                "<p>Thank you for choosing our service! We're excited to have you on board.</p>\n" +
                "<p>Your registration is almost complete. Please click the following link to activate your account:</p>\n" +
                "<p><a href=\"" + fullActivationLink + "\">Activation Link</a></p>\n" +
                "<p>If you have any questions, feel free to contact our support team.</p>\n" +
                "<p>Best regards,<br/>ISA project members</p>";
    }
}
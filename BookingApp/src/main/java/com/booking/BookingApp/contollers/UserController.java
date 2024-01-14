package com.booking.BookingApp.contollers;

import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.enums.StatusEnum;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.services.EmailService;
import com.booking.BookingApp.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;


import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private EmailService emailService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    //@CrossOrigin(origins = "*") // Postavite odgovarajuću putanju do vaše Angular aplikacije
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserGetDTO>> findAll(){
        List<UserGetDTO> users=userService.findAll();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping(value="/username/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    //@CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_GUEST','ROLE_OWNER')")
    public Optional<UserGetDTO> findById(@PathVariable String username){
        System.out.println("USAO U FIND USERNAME JE --> " +username);
        return userService.findById(username);
    }

    @GetMapping(value="/token/{token}",produces = MediaType.APPLICATION_JSON_VALUE)
    //@CrossOrigin(origins = "*")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_GUEST','ROLE_OWNER')")
    public Optional<User> findByToken(@PathVariable String token){
        System.out.println("USAO U FIND BY TOKEN");
        return userService.findByToken(token);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    //@CrossOrigin(origins = "*")
    public ResponseEntity<User> create(@RequestBody UserPostDTO newUser) throws Exception {
        Optional<UserGetDTO> optionalUser=userService.findById(newUser.username);
        if(optionalUser!=null){
            if(optionalUser.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//zato sto username mora biti jedinstven
            }
        }
        Optional<User> result=userService.create(newUser);
        if(result.isPresent()){
            System.out.println("NASAO SALJEM MEJL");
            User createdUser=result.get();
            emailService.send(createdUser.username,generateActivationEmailBody(createdUser.firstName+" "+createdUser.lastName,createdUser.token), "BookingApp - Confirm registration");
        }
        return new ResponseEntity<>(result.get(),HttpStatus.CREATED);

    }

    @PutMapping(value="/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    //@CrossOrigin(origins = "http://localhost:4200")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_GUEST','ROLE_OWNER')") //svi ulogovanmi
    public ResponseEntity<User> update(@RequestBody UserPutDTO user, @PathVariable String username) throws Exception {
        User result=userService.update(user,username);
        if(result==null){return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return new ResponseEntity<>(result,HttpStatus.OK);

    }
    @PutMapping(value="/addFavourite/{username}/{id}")
    //@CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_GUEST')")
    public ResponseEntity<?> addFavouriteAccommodation(@PathVariable String username, @PathVariable Long id){
        try{
            userService.addFavouriteAccommodation(username,id);
            return  new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @PutMapping(value="/removeFavourite/{username}/{id}")
   // @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ROLE_GUEST')")
    public ResponseEntity<?> removeFavouriteAccommodation(@PathVariable String username, @PathVariable Long id){
        try{
            userService.removeFavouriteAccommodation(username,id);
            return  new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @DeleteMapping(value="/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    //@CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_GUEST','ROLE_OWNER')")
    public ResponseEntity<?> delete(@PathVariable String username) throws Exception {
        System.out.print("USAO U BRISANJEEE " + username);
       try{
           userService.delete(username);
           return ResponseEntity.ok(username);
       }catch(Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(Collections.singletonMap("error", e.getMessage()));
       }
        //return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/activate/{token}")
    //@CrossOrigin(origins = "*")//* znaci da dozvoljava sa svih stranica
    public ResponseEntity<Map<String,String>> activateAccount(@PathVariable String token) throws Exception {
        List<UserGetDTO> allUsers=userService.findAll();
        for(UserGetDTO u:allUsers){
            if(u.token.equals(token)){
                User user=userService.findUserById(u.username);
                user.setStatus(StatusEnum.ACTIVE);
                UserPutDTO userPutDTO=new UserPutDTO(user.firstName,user.lastName,user.password,user.address,
                        user.phoneNumber,user.status,user.reservationRequestNotification,user.reservationCancellationNotification,user.ownerRatingNotification,
                        user.accommodationRatingNotification,user.ownerRepliedToRequestNotification,user.token,user.getDeleted(),false,user.getFavouriteAccommodations());
                userService.update(userPutDTO,user.username);
                return new ResponseEntity<>(Collections.singletonMap("message", "Account activated successfully"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Collections.singletonMap("message", "Invalid  token!"), HttpStatus.NOT_FOUND);

//        Optional<User> optionalUser = userService.findByToken(token);
//        System.out.println(token);
//        if (optionalUser.isPresent()) {
//            User user=optionalUser.get();
//            user.setStatus(StatusEnum.ACTIVE);
//            UserPutDTO userPutDTO=new UserPutDTO(user.firstName,user.lastName,user.password,user.address,
//                    user.phoneNumber,user.status,user.reservationRequestNotification,user.reservationCancellationNotification,user.ownerRatingNotification,
//                    user.accommodationRatingNotification,user.ownerRepliedToRequestNotification,user.token,user.getDeleted(),false,user.getFavouriteAccommodations());
//            userService.update(userPutDTO,user.username);
//            return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Invalid activation token", HttpStatus.NOT_FOUND);
//        }
    }
    public String generateActivationEmailBody(String userName, String activationLink) throws UnknownHostException {
        String hostAdress= InetAddress.getLocalHost().getHostAddress();
        String fullActivationLink = "http://192.168.1.36:4200/activate/"+activationLink;////OVDE STAVITE SVOJU IP ADRESU
        return "<p>Dear <strong>" + userName + "</strong>,</p>\n" +
                "<p>Thank you for choosing our service! We're excited to have you on board.</p>\n" +
                "<p>Your registration is almost complete. Please click the following link to activate your account:</p>\n" +
                "<p><a href=\"" + fullActivationLink + "\">Activation Link</a></p>\n" +
                "<p>If you have any questions, feel free to contact our support team.</p>\n" +
                "<p>Best regards,<br/>ISA project members</p>";
    }




    @GetMapping(value="user/username/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    //@CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_GUEST','ROLE_OWNER')")
    public User findUserById(@PathVariable String username){
        //System.out.println("USAO U FIND USERNAME JE --> " +username);
        return userService.findUserById(username);
    }

    @GetMapping(value="role/{role}",produces = MediaType.APPLICATION_JSON_VALUE)
   // @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> findByRole(@PathVariable RoleEnum role){
        List<User> result=userService.findByRole(role);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }


}
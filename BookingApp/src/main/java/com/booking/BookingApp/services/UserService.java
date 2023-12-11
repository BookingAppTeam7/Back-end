package com.booking.BookingApp.services;

import com.booking.BookingApp.models.enums.NotificationTypeEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.models.enums.StatusEnum;
import com.booking.BookingApp.repositories.IUserRepository;
import com.booking.BookingApp.security.jwt.JwtTokenUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService implements IUserService, UserDetailsService {
    @Autowired
    public IUserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    @Value("${jwt.secret}")
    private String secret;
    private static AtomicLong counter=new AtomicLong();

    @Override
    public List<User> findAll() {
        //List<User> result=userRepository.findAll();
        return userRepository.findAll();
       //List<UserGetDTO>resultDTO=new ArrayList<>();

        //for(User u:result){
        //    resultDTO.add(new UserGetDTO(u.getFirstName(),u.getLastName(),u.getUsername(),u.getRole(),u.getAddress(),u.getPhoneNumber(),u.getStatus(),u.getToken()));
        //}
        //return resultDTO;
    }

    @Override
    public Optional<User> findById(String username) {
        Optional<User> u=userRepository.findById(username);
        if(u.isPresent()) {
            return u;
        }
        return null;
    }

    @Override
    public Optional<User> create(UserPostDTO newUser) throws Exception {
        //Long newId= (Long) counter.incrementAndGet();
       //Map<NotificationTypeEnum,Boolean>notificationSettings=null;

      // String token = UUID.randomUUID().toString();
//        Map<String, Object> claims = new HashMap<>();
//
//
//        String token= Jwts.builder().setClaims(claims).setSubject(newUser.username).setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//                .signWith(SignatureAlgorithm.HS512, secret).compact();
        String token = jwtTokenUtil.generateToken(newUser.getUsername());
        User createdUser=new User(newUser.firstName, newUser.lastName,newUser.username, newUser.password, newUser.role,newUser.address,newUser.phoneNumber, StatusEnum.DEACTIVE,newUser.reservationRequestNotification,
                newUser.reservationCancellationNotification,newUser.ownerRatingNotification,newUser.accommodationRatingNotification,newUser.ownerRepliedToRequestNotification, token, newUser.deleted);
        return Optional.of(userRepository.save(createdUser));
    }

    @Override
    public User update(UserPutDTO updatedUser, String username) throws Exception {
        User result=new User(updatedUser.firstName, updatedUser.lastName,username, updatedUser.password, updatedUser.role,updatedUser.address,updatedUser.phoneNumber, updatedUser.status,
                updatedUser.reservationRequestNotification,updatedUser.reservationCancellationNotification,updatedUser.ownerRatingNotification,
                updatedUser.accommodationRatingNotification, updatedUser.ownerRepliedToRequestNotification, updatedUser.token, updatedUser.deleted);
        return userRepository.saveAndFlush(result);
    }

    @Override
    public void delete(String username) {
        userRepository.deleteById(username);
    }

    @Override
    public Optional<User> findByToken(String token){
        return userRepository.findByToken(token);
    }
    @Override
    public Optional<User> save(User user){
        return Optional.of(userRepository.save(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> ret = userRepository.findById(username);
        if (!ret.isEmpty() ) {
            return org.springframework.security.core.userdetails.User.withUsername(username).password(ret.get().getPassword()).roles(ret.get().getRole().toString()).build();
        }
        throw new UsernameNotFoundException("User not found with this username: " + username);
    }
}

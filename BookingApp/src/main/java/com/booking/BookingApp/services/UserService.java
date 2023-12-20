package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.enums.NotificationTypeEnum;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPutDTO;
import com.booking.BookingApp.models.enums.StatusEnum;
import com.booking.BookingApp.repositories.IAccommodationRepository;
import com.booking.BookingApp.repositories.IReservationRepository;
import com.booking.BookingApp.repositories.IUserRepository;
import com.booking.BookingApp.security.jwt.JwtTokenUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService implements IUserService{
    @Autowired
    public IUserRepository userRepository;
    @Autowired

    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IReservationRepository reservationRepository;
    @Autowired
    private AccommodationService accommodationService;


    public UserService(){

    }

    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
  
    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    public IUserValidatorService userValidatorService;

    @Autowired
    public IAccommodationRepository accommodationRepository;

    private static AtomicLong counter=new AtomicLong();

    @Override
    public List<UserGetDTO> findAll() {
        List<User> result=userRepository.findAll();
        //return userRepository.findAll();
        List<UserGetDTO>resultDTO=new ArrayList<>();

        for(User u:result){
            resultDTO.add(new UserGetDTO(u.getFirstName(),u.getLastName(),u.getUsername(),u.getRole(),u.getAddress(),
                    u.getPhoneNumber(),u.getStatus(),u.getDeleted(),u.getReservationRequestNotification(),
                    u.getReservationCancellationNotification(),u.getOwnerRatingNotification(),u.getAccommodationRatingNotification()
                    ,u.getOwnerRepliedToRequestNotification(),u.getToken(),u.getJwt()));
        }
        return resultDTO;
    }

    @Override
    public Optional<UserGetDTO> findById(String username) {
        Optional<User> res=userRepository.findById(username);
        if(res.isPresent()) {
            User u=res.get();
            return Optional.of(new UserGetDTO(u.getFirstName(),u.getLastName(),u.getUsername(),u.getRole(),u.getAddress(),
                    u.getPhoneNumber(),u.getStatus(),u.getDeleted(),u.getReservationRequestNotification(),
                    u.getReservationCancellationNotification(),u.getOwnerRatingNotification(),u.getAccommodationRatingNotification()
                    ,u.getOwnerRepliedToRequestNotification(),u.getToken(),u.getJwt()));
        }
        return null;
    }

    @Override
    public User findUserById(String username) {
        Optional<User> res=userRepository.findById(username);
        if(res.isPresent()) {
         return res.get();
        }
        return null;
    }

    @Override
    public Optional<User> create(UserPostDTO newUser) throws Exception {

        String token = jwtTokenUtil.generateToken(newUser.getUsername());
        userValidatorService.validatePost(newUser);
        //Long newId= (Long) counter.incrementAndGet()
        User createdUser=new User(newUser.firstName, newUser.lastName,newUser.username, newUser.password, newUser.role,newUser.address,newUser.phoneNumber, StatusEnum.DEACTIVE,newUser.reservationRequestNotification,
                newUser.reservationCancellationNotification,newUser.ownerRatingNotification,newUser.accommodationRatingNotification,newUser.ownerRepliedToRequestNotification, token, newUser.deleted);
        createdUser.setPassword(passwordEncoder.encode(createdUser.password));
        return Optional.of(userRepository.save(createdUser));
    }

    @Override
    public User update(UserPutDTO updatedUser, String username) throws Exception {
        userValidatorService.validatePut(updatedUser,username);
        Optional<UserGetDTO> updatedUserRole=findById(username);
        User result=new User(updatedUser.firstName, updatedUser.lastName,username, updatedUser.password, updatedUserRole.get().role,updatedUser.address,updatedUser.phoneNumber, updatedUser.status,
                updatedUser.reservationRequestNotification,updatedUser.reservationCancellationNotification,updatedUser.ownerRatingNotification,
                updatedUser.accommodationRatingNotification, updatedUser.ownerRepliedToRequestNotification, updatedUser.token, updatedUser.deleted);
        result.setPassword(passwordEncoder.encode(result.password));
        return userRepository.saveAndFlush(result);
    }

    @Override
    public void delete(String username) throws Exception {
        //proveravamo dal moze
        Optional<UserGetDTO> userToDelete=findById(username);
        System.out.println("USER TO DELEETEEE USERNAMEEEE ---> "+userToDelete.get().username);
//        Gost može obrisati nalog ako nema aktivnih rezervacija. Vlasnik može obrisati nalog samo ako
//        nema aktivnih rezervacija u budućnosti ni za jedan smeštaj kojim upravlja. Kada vlasnik obriše
//        nalog, uklanjaju se i svi smeštaji koje je kreirao.
        if(userToDelete.get().role.equals( RoleEnum.GUEST)){
            System.out.println("UUSAO U IF ZA GUEST ROLE ---> "+userToDelete.get().role);
            List<Reservation> reservations=reservationRepository.findByUserUsername(userToDelete.get().username);
            for(Reservation res : reservations){
                System.out.println("UUSAO U FOR--> "+userToDelete.get().username);
                if(res.status.equals( ReservationStatusEnum.APPROVED) ||
                res.status.equals(ReservationStatusEnum.PENDING) || res.status.equals(ReservationStatusEnum.INPROCESS)){
                    System.out.println("Guest cannot be deleted");
                    throw new  Exception("Guest cannot be deleted");
                   // return;
                }
            }
        }
        if(userToDelete.get().role.equals(RoleEnum.OWNER)){
            System.out.println("OWNERRRRR USAO IF USERNAME ->  " + userToDelete.get().username);
            List<Reservation> reservations=reservationRepository.findAll();
            for(Reservation res: reservations){
                System.out.println("OWNERRRRR USAO FOR REZERVACIJEEE "  );
                Accommodation accommodation=res.accommodation;
                if(accommodation.ownerId.equals(userToDelete.get().username) &&
                res.getTimeSlot().startDate.after(new Date()) && ( res.status.equals( ReservationStatusEnum.APPROVED )||
                        res.status.equals(ReservationStatusEnum.PENDING) || res.status.equals(ReservationStatusEnum.INPROCESS))){
                    System.out.println("RESSS STATUS " +res.status);
                    System.out.println("Owner cannot be deleted");
                    throw new  Exception("Owner cannot be deleted");
                    //return;
                }else{
                    //brisemo sve smestaje koje je kreirao owner
                    List<Accommodation> accomodations=accommodationRepository.findAll();
                    for(Accommodation acc: accomodations){
                        if(acc.ownerId.equals(userToDelete.get().username)){
                            accommodationRepository.deleteById(acc.id);
                        }
                    }

                }
            }

        }
        userRepository.deleteById(username);
    }

    @Override
    public Optional<User> findByToken(String token){
        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            if (user.getToken().equals(token)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }
    @Override
    public Optional<User> save(User user){
        return Optional.of(userRepository.save(user));
    }


}

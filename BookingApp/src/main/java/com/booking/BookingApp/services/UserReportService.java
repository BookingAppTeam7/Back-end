package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.dtos.reports.UserReportPostDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationGetDTO;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.enums.StatusEnum;
import com.booking.BookingApp.models.reports.UserReport;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IUserReportRepository;
import com.booking.BookingApp.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserReportService implements  IUserReportService{

    @Autowired
    public IUserReportRepository userReportRepository;
    @Autowired
    public IUserRepository userRepository;
    @Autowired
    public IUserService userService;
    @Autowired
    public IReservationService reservationService;
    @Override
    public Optional<UserReport> create(UserReportPostDTO newUserReport) throws Exception {
        Optional<UserGetDTO> userThatReports=userService.findById(newUserReport.userThatReportsId);
        Optional<UserGetDTO> userThatIsReported=userService.findById(newUserReport.userThatIsReportedId);
        //gost prijavio vlasnika
        if(userThatReports.get().role.equals( RoleEnum.GUEST) && userThatIsReported.get().role.equals(RoleEnum.OWNER)){
            List<Reservation> allReservations=reservationService.findAll();
            for(Reservation res:allReservations){
                if(res.accommodation.ownerId.equals(newUserReport.userThatIsReportedId)
                && res.user.username.equals(newUserReport.userThatReportsId)
                && res.status.equals(ReservationStatusEnum.APPROVED)){
                    UserReport userReport=new UserReport(newUserReport.userThatReportsId,newUserReport.userThatIsReportedId, newUserReport.reason
                            ,false);
                    userReportRepository.save(userReport);
                    return  Optional.of(userReport);
                }
            }


        }

        if(userThatReports.get().role.equals( RoleEnum.OWNER) && userThatIsReported.get().role.equals(RoleEnum.GUEST)){
            List<Reservation> allReservations=reservationService.findAll();
            for(Reservation res:allReservations){
                if(res.accommodation.ownerId.equals(newUserReport.userThatReportsId)
                        && res.user.username.equals(newUserReport.userThatIsReportedId) && res.status.equals(ReservationStatusEnum.APPROVED)){
                    UserReport userReport=new UserReport(newUserReport.userThatReportsId,newUserReport.userThatIsReportedId, newUserReport.reason,
                            false);
                    userReportRepository.save(userReport);
                    return  Optional.of(userReport);
                }
            }


        }
        return  null;
    }

    @Override
    public List<UserReport> findAll() {
        return userReportRepository.findAll();
    }

    @Override
    public UserReport report(Long requestId) {
        Optional<UserReport> report=userReportRepository.findById(requestId);
        if(!report.isPresent()){
            return null;
        }
        Optional<User> userToReport=userRepository.findById(report.get().getUserThatIsReported());
        if(!userToReport.isPresent()){
            return null;
        }

        userRepository.updateStatus(userToReport.get().username,StatusEnum.DEACTIVE);
        userReportRepository.updateDone(requestId);

        return userReportRepository.getById(requestId);
    }

    @Override
    public UserReport ignore(Long requestId) {
        Optional<UserReport> report=userReportRepository.findById(requestId);
        if(!report.isPresent()){
            return null;
        }
        Optional<User> userToReport=userRepository.findById(report.get().getUserThatIsReported());
        if(!userToReport.isPresent()){
            return null;
        }

        userReportRepository.updateDone(requestId);

        return userReportRepository.getById(requestId);
    }

    @Override
    public List<UserReport> findByUser(String userId) {
        Optional<User> user=userRepository.findById(userId);
        if(!user.isPresent()){
            return  null;
        }

        return userReportRepository.findByUserThatIsReported(userId);
    }

}

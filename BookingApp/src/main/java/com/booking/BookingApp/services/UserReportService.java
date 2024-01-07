package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.dtos.reports.UserReportPostDTO;
import com.booking.BookingApp.models.dtos.reservations.ReservationGetDTO;
import com.booking.BookingApp.models.dtos.users.UserGetDTO;
import com.booking.BookingApp.models.enums.ReservationStatusEnum;
import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.reports.UserReport;
import com.booking.BookingApp.models.reservations.Reservation;
import com.booking.BookingApp.models.users.User;
import com.booking.BookingApp.repositories.IUserReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserReportService implements  IUserReportService{

    @Autowired
    public IUserReportRepository userReportRepository;
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
}

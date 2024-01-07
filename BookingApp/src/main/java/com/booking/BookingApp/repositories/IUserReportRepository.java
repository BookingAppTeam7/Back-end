package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.reports.UserReport;
import com.booking.BookingApp.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IUserReportRepository extends JpaRepository<UserReport,Long> {


}

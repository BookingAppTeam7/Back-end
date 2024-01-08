package com.booking.BookingApp.services;

import com.booking.BookingApp.models.dtos.reports.UserReportPostDTO;
import com.booking.BookingApp.models.dtos.users.UserPostDTO;
import com.booking.BookingApp.models.reports.UserReport;
import com.booking.BookingApp.models.users.User;

import java.util.List;
import java.util.Optional;

public interface IUserReportService {
    Optional<UserReport> create(UserReportPostDTO newUserReport) throws Exception;
    List<UserReport> findAll();

    UserReport report(Long requestId) throws Exception;
    UserReport ignore(Long requestId);

    List<UserReport> findByUser(String userId);
}

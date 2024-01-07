package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.enums.RoleEnum;
import com.booking.BookingApp.models.reports.UserReport;
import com.booking.BookingApp.models.users.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IUserReportRepository extends JpaRepository<UserReport,Long> {

    @Modifying
    @Transactional
    @Query("UPDATE UserReport ur SET ur.done = true WHERE ur.id = :reportId")
    int updateDone(@Param("reportId") Long reportId);

}

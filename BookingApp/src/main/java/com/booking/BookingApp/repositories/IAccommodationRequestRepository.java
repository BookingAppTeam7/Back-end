package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.AccommodationRequest;
import com.booking.BookingApp.models.enums.AccommodationRequestStatus;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IAccommodationRequestRepository extends JpaRepository<AccommodationRequest,Long> {
    List<AccommodationRequest> findByRequestStatus(AccommodationRequestStatus status);
    @Modifying
    @Transactional
    @Query("UPDATE AccommodationRequest r SET r.requestStatus = :status WHERE r.id = :requestId")
    int updateStatus(@Param("requestId") Long requestId, @Param("status") AccommodationRequestStatus status);

    List<AccommodationRequest> findByRequestStatusIn(List<AccommodationRequestStatus> statuses);

}

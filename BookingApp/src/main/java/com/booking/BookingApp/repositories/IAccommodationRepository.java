package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAccommodationRepository extends JpaRepository<Accommodation,Long> {
    List<Accommodation> findByStatus(AccommodationStatusEnum status);

    @Modifying
    @Transactional
    @Query("UPDATE Accommodation a SET a.status = :status WHERE a.id = :accommodationId")
    int updateStatus(@Param("accommodationId") Long accommodationId, @Param("status") AccommodationStatusEnum status);


}

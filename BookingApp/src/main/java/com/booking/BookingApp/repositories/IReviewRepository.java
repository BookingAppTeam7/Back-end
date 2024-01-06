package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.accommodations.Review;
import com.booking.BookingApp.models.enums.ReviewStatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findByOwnerId(String ownerId);
    List<Review> findByAccommodationId(Long accommodationId);

    List<Review> findByOwnerIdAndStatus(String ownerId, ReviewStatusEnum status);

    List<Review> findByAccommodationIdAndStatus(Long accommodationId, ReviewStatusEnum status);

    @Modifying
    @Transactional
    @Query("UPDATE Review r SET r.status = :status WHERE r.id = :reviewId")
    int updateStatus(@Param("reviewId") Long reviewId, @Param("status") ReviewStatusEnum status);

}

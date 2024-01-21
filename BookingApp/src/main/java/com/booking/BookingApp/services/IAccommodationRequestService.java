package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.Accommodation;
import com.booking.BookingApp.models.accommodations.AccommodationRequest;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationPutDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationRequestPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationRequestPutDTO;
import com.booking.BookingApp.models.enums.AccommodationRequestStatus;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;

import java.util.List;
import java.util.Optional;

public interface IAccommodationRequestService {

    List<AccommodationRequest> findAll();
    Optional<AccommodationRequest> findById(Long id);
    List<AccommodationRequest> findByStatus(AccommodationRequestStatus status);

    Optional<AccommodationRequest> create(AccommodationRequestPostDTO newRequest) throws Exception;
    Optional<AccommodationRequest> update(AccommodationRequestPutDTO updatedRequest, Long  id) throws  Exception;

    Optional<AccommodationRequest> updateStatus(Long requestId,AccommodationRequestStatus status);
    List<AccommodationRequest> findByRequestStatus(AccommodationRequestStatus status1, AccommodationRequestStatus status2);

}

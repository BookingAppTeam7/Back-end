package com.booking.BookingApp.services;

import com.booking.BookingApp.models.dtos.accommodations.FeedbackPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.FeedbackPutDTO;
import com.booking.BookingApp.models.reservations.Feedback;
import com.booking.BookingApp.models.users.User;

import java.util.List;
import java.util.Optional;

public interface IFeedbackService {
    List<Feedback> findAll();

    Optional<Feedback> findById(Long id);

    Optional<Feedback> create(FeedbackPostDTO newFeedback) throws Exception;
    Feedback update(FeedbackPutDTO updatedFeedback, Long  id) throws  Exception;
    void delete(Long id);
}

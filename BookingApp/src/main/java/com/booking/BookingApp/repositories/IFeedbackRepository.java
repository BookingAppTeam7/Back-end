package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.reservations.Feedback;
import com.booking.BookingApp.models.users.User;

import java.util.List;
import java.util.Optional;

public interface IFeedbackRepository {

    List<Feedback> findAll();

    Optional<Feedback> findById(Long id);

    Optional<Feedback> save(Feedback createdFeedback);

    Feedback saveAndFlush(Feedback result);

    void deleteById(Long id);
}

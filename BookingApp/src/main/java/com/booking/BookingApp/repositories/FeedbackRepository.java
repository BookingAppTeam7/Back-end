package com.booking.BookingApp.repositories;

import com.booking.BookingApp.models.reservations.Feedback;
import com.booking.BookingApp.models.users.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FeedbackRepository implements IFeedbackRepository{
    private List<Feedback> feedbacks=new ArrayList<>();
    private static AtomicLong counter=new AtomicLong();
    @Override
    public List<Feedback> findAll() {
        return this.feedbacks;
    }

    public Optional<Feedback> getById(Long id){
        return feedbacks.stream().filter(feedback -> feedback.getId()== id).findFirst();
    }

    @Override
    public Optional<Feedback> findById(Long id) {
        return feedbacks.stream().filter(feedback -> feedback.getId() == id).findFirst();
    }

    @Override
    public Optional<Feedback> save(Feedback newFeedback) {
        this.feedbacks.add(newFeedback);
        return getById(newFeedback.getId());
    }

    @Override
    public Feedback saveAndFlush(Feedback updatedFeedback) {
        Optional<Feedback> optionalFeedback=getById(updatedFeedback.id);

        if (optionalFeedback.isPresent()) {
            Feedback feedback = optionalFeedback.get();
            feedback.setAccommodationId(updatedFeedback.accommodationId);
            feedback.setReviews(updatedFeedback.getReviews());

            return feedback;
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        Optional<Feedback> feedback=getById(id);
        if(feedback.isPresent()){
            Feedback feedbackToDelete = feedback.get();
            feedbacks.remove(feedbackToDelete);
        }
    }

}
